package com.example.assignmentc.ui.screens

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
import com.example.assignmentc.logic.EnemyManager
import com.example.assignmentc.logic.GameManager
import com.example.assignmentc.logic.Maze
import com.example.assignmentc.logic.PlayerManager
import com.example.assignmentc.logic.TempleMaze
import com.example.assignmentc.ui.components.MazeDisplay
import com.example.assignmentc.ui.components.MovementButtons

@Composable
fun GameScreen(onNavigateToLeaderboard: () -> Unit) {
    var context = LocalContext.current
    var maze: Maze by remember { mutableStateOf(TempleMaze()) }
    /*val playerManager: PlayerManager by remember { mutableStateOf(PlayerManager(context,maze)) }
    playerManager.spawnPlayer()
    val enemyManager: EnemyManager by remember { mutableStateOf(EnemyManager(context,maze)) }
    enemyManager.spawnEnemies()*/

    val gameManager by remember { mutableStateOf(GameManager(context,maze)) }
    gameManager.StartGame()

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

                    gameManager.movePlayer(direction)
                    maze = maze.copySelf()
                    gameManager.Update()
                },
                onShowLeaderboard = onNavigateToLeaderboard
            )
        }
    }
}