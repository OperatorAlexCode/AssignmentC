package com.example.assignmentc.ui.screens

import android.media.MediaPlayer
import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignmentc.R
import com.example.assignmentc.logic.other.Maze
import com.example.assignmentc.ui.components.HealthBar
import com.example.assignmentc.ui.components.ItemBox
import com.example.assignmentc.ui.components.MazeDisplay
import com.example.assignmentc.ui.components.MovementButtons
import com.example.assignmentc.ui.components.ScoreDisplay
import com.example.assignmentc.ui.viewmodels.GameScreenViewModel
import com.example.assignmentc.ui.viewmodels.GameScreenViewModelFactory

@Composable
fun GameScreen(
    onNavigateToDeathScreen: (Int) -> Unit,
    initialMaze: Maze,
    viewModel: GameScreenViewModel = viewModel(
        factory = GameScreenViewModelFactory(
            LocalContext.current.applicationContext as Application,
            initialMaze
        )
    )
) {
    var context = LocalContext.current
    val maze by viewModel.maze
    val isGameOver by viewModel.isGameOver

    LaunchedEffect(isGameOver) {
        if (isGameOver) {
            val score = viewModel.getGameManager().score
            onNavigateToDeathScreen(score)
        }
    }

    val gameManager = viewModel.getGameManager()
    var walksfx = MediaPlayer.create(context, R.raw.footstep)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            // Main content (centered)
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ScoreDisplay(score = gameManager.score)
                MazeDisplay(Modifier.padding(16.dp), maze, gameManager)
                Spacer(modifier = Modifier.height(30.dp))
                Row {
                    MovementButtons(onMove = { direction ->
                        viewModel.movePlayer(direction)
                        if (walksfx.isPlaying) walksfx.seekTo(0)
                        else walksfx.start()
                    })
                    Spacer(Modifier.size(30.dp))
                    ItemBox(
                        item = gameManager.getHeldItem(),
                        onClick = { gameManager.useHeldItem() }
                    )
                }
            }

            // Health bar (top center overlay)
            HealthBar(
                health = viewModel.playerHealth.value,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            )
        }
    }
}
