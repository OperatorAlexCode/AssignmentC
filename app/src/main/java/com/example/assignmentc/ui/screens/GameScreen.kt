package com.example.assignmentc.ui.screens

import android.media.MediaPlayer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignmentc.R
import com.example.assignmentc.logic.Maze
import com.example.assignmentc.ui.components.MazeDisplay
import com.example.assignmentc.ui.components.MovementButtons
import com.example.assignmentc.ui.viewmodels.GameScreenViewModel
import com.example.assignmentc.ui.viewmodels.GameScreenViewModelFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color






@Composable
fun GameScreen(
    onNavigateToLeaderboard: () -> Unit,
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
    /*val playerManager: PlayerManager by remember { mutableStateOf(PlayerManager(context,maze)) }
    playerManager.spawnPlayer()
    val enemyManager: EnemyManager by remember { mutableStateOf(EnemyManager(context,maze)) }
    enemyManager.spawnEnemies()*/

    //val gameManager by remember { mutableStateOf(GameManager(context,maze)) }
    //gameManager.StartGame()
    val gameManager = viewModel.getGameManager()
    //viewModel.StartGame(maze)

    //val playerManager = viewModel.getPlayerManager()
    //val enemyManager = viewModel.getEnemyManager()

    var walksfx = MediaPlayer.create(context,R.raw.footstep)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MazeDisplay(Modifier.padding(16.dp), maze, gameManager/*playerManager,enemyManager*/)
            Spacer(modifier = Modifier.height(30.dp))
            MovementButtons(
                onMove = { direction ->
                    //playerManager.movePlayer(direction)
                    //maze = maze.copySelf()
                    //enemyManager.moveAllEnemies()

                    //gameManager.movePlayer(direction)
                    //maze = maze.copySelf()
                    //gameManager.Update()
                    viewModel.movePlayer(direction)

                    if (walksfx.isPlaying)
                        walksfx.seekTo(0)
                    else
                        walksfx.start()
                },
                onUseItem = {viewModel.useItem()},
                onShowLeaderboard = onNavigateToLeaderboard,
                useEnabled = viewModel.hasHeldItem.value
            )
        }
    }
}
