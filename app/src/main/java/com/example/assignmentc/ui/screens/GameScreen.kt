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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignmentc.logic.EnemyManager
import com.example.assignmentc.logic.GameManager
import com.example.assignmentc.logic.Maze
import com.example.assignmentc.logic.PlayerManager
import com.example.assignmentc.logic.TempleMaze
import com.example.assignmentc.R
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
fun GameScreen(onNavigateToLeaderboard: () -> Unit, viewModel: GameScreenViewModel = viewModel(factory = GameScreenViewModelFactory(LocalContext.current.applicationContext as Application))) {
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



            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                    onUseItem = { viewModel.useItem() },
                    onShowLeaderboard = onNavigateToLeaderboard,
                    useEnabled = viewModel.hasHeldItem.value
                )

                Spacer(modifier = Modifier.width(24.dp))

                // Shows current held item or remains empty
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .border(
                            BorderStroke(2.dp, Color.LightGray),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                        )
                        .background(Color(0xFF1F1B24))
                ) {
                    // If an item is held, sprite shows centered in the box
                    viewModel.heldItemRes.value?.let { resId ->
                        Image(
                            painter = painterResource(resId),
                            contentDescription = "Held Item",
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}