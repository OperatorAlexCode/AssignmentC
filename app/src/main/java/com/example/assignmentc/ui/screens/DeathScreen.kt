package com.example.assignmentc.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignmentc.ui.viewmodels.LeaderboardViewModel
import com.example.assignmentc.R
import com.example.assignmentc.ui.viewmodels.LeaderboardViewModelFactory

// DeathScreen.kt
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun DeathScreen(
    score: Int,
    onNavigateToLeaderboard: () -> Unit,
    onGoToMenu: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: LeaderboardViewModel = viewModel(
        factory = LeaderboardViewModelFactory(context)
    )
    var name by remember { mutableStateOf("") }
    var submitOnline by remember { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Game Over",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Score Display
            Text(
                text = "Score $score",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Enemy Icon
            EnemySprite(Modifier.size(150.dp))

            Spacer(modifier = Modifier.height(32.dp))

            // Name Input
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Your Name") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(bottom = 16.dp)
            )

            // Online Submission Checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Checkbox(
                    checked = submitOnline,
                    onCheckedChange = { submitOnline = it }
                )
                Text("Submit online", modifier = Modifier.padding(start = 8.dp))
            }

            // Submit Button
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        viewModel.submitLocalScore(name, score)
                        if (submitOnline) {
                            viewModel.submitOnlineScore(name, score)
                        }
                        submitted = true
                    }
                },
                modifier = Modifier.width(200.dp),
                enabled = !submitted && name.isNotBlank()
            ) {
                Text(if (submitted) "Submitted!" else "Submit Score")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation Buttons
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onGoToMenu) {
                    Text("Main Menu")
                }
                Button(onClick = onNavigateToLeaderboard) {
                    Text("Leaderboard")
                }
            }

            // Error Message
            if (viewModel.errorMessage.value.isNotBlank()) {
                Text(
                    text = viewModel.errorMessage.value,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

// DeathScreen.kt
@Composable
fun EnemySprite(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val bitmap = remember {
        val spriteSheet = BitmapFactory.decodeResource(context.resources, R.drawable.enemy)
        val rows = 4
        val columns = 2
        val frameWidth = spriteSheet.width / columns
        val frameHeight = spriteSheet.height / rows

        Bitmap.createBitmap(
            spriteSheet,
            frameWidth * 1,   // Column index for enemy frame
            frameHeight * 2,  // Row index for enemy frame
            frameWidth,
            frameHeight
        )
    }

    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "Enemy Character",
        modifier = modifier,
        contentScale = ContentScale.Fit,
        filterQuality = FilterQuality.None
    )
}