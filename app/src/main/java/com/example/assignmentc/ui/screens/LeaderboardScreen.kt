package com.example.assignmentc.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.assignmentc.data.AppDatabase
import com.example.assignmentc.data.ScoreRepository
import com.example.assignmentc.ui.components.ScoreEntryRow
import com.example.assignmentc.ui.viewmodels.LeaderboardViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignmentc.data.FirestoreRepository
import com.example.assignmentc.ui.components.AddScoreDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    onGoToMenu: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: LeaderboardViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = AppDatabase.getDatabase(context)
                val localRepo = ScoreRepository(database.scoreDao())
                val firestoreRepo = FirestoreRepository()
                return LeaderboardViewModel(localRepo, firestoreRepo) as T
            }
        }
    )

    var showAddScoreDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("local") }
    val localScores by viewModel.localScores.collectAsState(initial = emptyList())
    val onlineScores by viewModel.onlineScores.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Auto-load online scores when tab is switched
    LaunchedEffect(selectedTab) {
        if (selectedTab == "online" && onlineScores.isEmpty()) {
            viewModel.loadOnlineScores()
        }
    }

    // Snackbar setup
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Show error messages in snackbar
    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(errorMessage)
                viewModel.clearError()
            }
        }
    }

    if (showAddScoreDialog) {
        AddScoreDialog(
            onDismiss = { showAddScoreDialog = false },
            onSave = { name, score ->
                if (selectedTab == "local") {
                    viewModel.submitLocalScore(name, score)
                } else {
                    viewModel.submitOnlineScore(name, score)
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TabButton(
                    text = "Local",
                    isSelected = selectedTab == "local",
                    onClick = { selectedTab = "local" }
                )
                TabButton(
                    text = "Online",
                    isSelected = selectedTab == "online",
                    onClick = { selectedTab = "online" }
                )
            }

            // Refresh button - only for online tab
            if (selectedTab == "online") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { viewModel.loadOnlineScores() },
                        enabled = !isLoading,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh"
                            )
                        }
                        Spacer(Modifier.width(4.dp))
                        Text("Refresh Online")
                    }
                }
            }

            // Score list
            if (selectedTab == "online" && onlineScores.isEmpty() && isLoading) {
                // Loading indicator when fetching online scores
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            } else if ((selectedTab == "local" && localScores.isEmpty()) ||
                (selectedTab == "online" && onlineScores.isEmpty())) {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "No scores available",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    // FIX: Use unique keys for items
                    items(
                        items = if (selectedTab == "local") localScores else onlineScores,
                        key = { item ->
                            // Create a unique key using name + score + a hash of the ID
                            "${item.name}-${item.score}-${item.id.hashCode()}"
                        }
                    ) { entry ->
                        ScoreEntryRow(entry = entry)
                    }
                }
            }

            // Bottom buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showAddScoreDialog = true },
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text("Add Score")
                }

                Button(
                    onClick = onGoToMenu,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text("Start Menu")
                }
            }
        }
    }
}

@Composable
private fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        modifier = modifier.width(120.dp)
    ) {
        Text(text = text)
    }
}