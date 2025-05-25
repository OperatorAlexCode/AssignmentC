package com.example.assignmentc.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@Composable
fun LeaderboardScreen() {
    val context = LocalContext.current
    val viewModel: LeaderboardViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                // Use the context captured outside the factory
                val database = AppDatabase.getDatabase(context.applicationContext)
                val repository = ScoreRepository(database.scoreDao())
                @Suppress("UNCHECKED_CAST")
                return LeaderboardViewModel(repository) as T
            }
        }
    )

    var selectedTab by remember { mutableStateOf("local") }
    val localScores by viewModel.localScores.collectAsState(initial = emptyList())
    val onlineScores by viewModel.onlineScores.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
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
                onClick = {
                    selectedTab = "online"
                    viewModel.loadOnlineScores()
                }
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(
                items = if (selectedTab == "local") localScores else onlineScores,
                key = { it.name }
            ) { entry ->
                ScoreEntryRow(entry = entry)
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