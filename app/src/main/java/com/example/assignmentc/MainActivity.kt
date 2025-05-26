package com.example.assignmentc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.assignmentc.ui.navigation.Screen
import com.example.assignmentc.ui.screens.GameScreen
import com.example.assignmentc.ui.screens.LeaderboardScreen
import com.example.assignmentc.ui.theme.AssignmentCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var maze:Maze by remember { mutableStateOf(TempleMaze()) }
            val playerManager: PlayerManager by remember { mutableStateOf(PlayerManager(maze))}
            playerManager.spawnPlayer()
            val enemyManager: EnemyManager by remember { mutableStateOf(EnemyManager(maze))}
            enemyManager.spawnEnemies()

            AssignmentCTheme {
                MazeNavigationApp()
            }
        }
    }
}

@Composable
fun MazeNavigationApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Game.route
    ) {
        composable(Screen.Game.route) {
            GameScreen(
                onNavigateToLeaderboard = { navController.navigate(Screen.Leaderboard.route) }
            )
        }
        composable(Screen.Leaderboard.route) {
            LeaderboardScreen(onBack = { navController.popBackStack() })
        }
    }
}