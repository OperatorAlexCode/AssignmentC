package com.example.assignmentc.ui.screens

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignmentc.logic.EnemyManager
import com.example.assignmentc.logic.Maze
import com.example.assignmentc.logic.PlayerManager
import com.example.assignmentc.logic.TempleMaze
import com.example.assignmentc.ui.components.MazeDisplay
import com.example.assignmentc.ui.components.MovementButtons
import com.example.assignmentc.ui.viewmodels.GameScreenViewModel
import com.example.assignmentc.ui.viewmodels.GameScreenViewModelFactory

@Composable
fun GameScreen(onNavigateToLeaderboard: () -> Unit, viewModel: GameScreenViewModel = viewModel(factory = GameScreenViewModelFactory(LocalContext.current.applicationContext as Application))) {
    val maze by viewModel.maze
    val playerManager = viewModel.getPlayerManager()
    val enemyManager = viewModel.getEnemyManager()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MazeDisplay(Modifier.padding(16.dp), maze,playerManager,enemyManager)
            Spacer(modifier = Modifier.height(30.dp))
            MovementButtons(
                onMove = { direction ->
                    viewModel.movePlayer(direction)
                },
                onShowLeaderboard = onNavigateToLeaderboard
            )
        }
    }
}