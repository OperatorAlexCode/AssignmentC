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
import com.example.assignmentc.ui.screens.StartScreen
import com.example.assignmentc.ui.theme.AssignmentCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
        startDestination = Screen.Start.route
    ) {
        composable(Screen.Start.route) {
            StartScreen(
                onStartGame = { navController.navigate(Screen.Game.route) },
                onShowLeaderboard = { navController.navigate(Screen.Leaderboard.route) }
            )
        }
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