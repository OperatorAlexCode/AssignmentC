package com.example.assignmentc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.assignmentc.logic.other.BlockMaze
import com.example.assignmentc.logic.other.LineMaze
import com.example.assignmentc.logic.other.Maze
import com.example.assignmentc.logic.other.TempleMaze
import com.example.assignmentc.ui.navigation.Screen
import com.example.assignmentc.ui.screens.DeathScreen
import com.example.assignmentc.ui.screens.GameScreen
import com.example.assignmentc.ui.screens.LeaderboardScreen
import com.example.assignmentc.ui.screens.MazePickerScreen
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
                onStartGame = { navController.navigate(Screen.MazePicker.route) },
                onShowLeaderboard = { navController.navigate(Screen.Leaderboard.route) }
            )
        }
        composable(Screen.MazePicker.route) {
            MazePickerScreen(
                onMazeSelected = { maze ->
                    // Pass selected maze to GameScreen
                    navController.navigate("${Screen.Game.route}/${maze::class.simpleName}")
                },
                onCancel = { navController.popBackStack() }
            )
        }
        composable(
            route = "${Screen.Game.route}/{mazeType}",
            arguments = listOf(navArgument("mazeType") { type = NavType.StringType })
        ) { backStackEntry ->
            val mazeType = backStackEntry.arguments?.getString("mazeType")
            val maze = when(mazeType) {
                "BlockMaze" -> BlockMaze()
                "LineMaze" -> LineMaze()
                "TempleMaze" -> TempleMaze()
                else -> Maze()
            }

            GameScreen(
                onNavigateToDeathScreen = { score ->
                    navController.navigate("${Screen.Death.route}/$score")
                },
                initialMaze = maze
            )
        }
        composable(Screen.Leaderboard.route) {
            LeaderboardScreen(onGoToMenu = { navController.navigate(Screen.Start.route) })
        }
        composable(
            route = Screen.Death.route + "/{score}",
            arguments = listOf(navArgument("score") { type = NavType.IntType })
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            DeathScreen(
                score = score,
                onNavigateToLeaderboard = { navController.navigate(Screen.Leaderboard.route) },
                onGoToMenu = { navController.popBackStack(Screen.Start.route, false) }
            )
        }
    }
}