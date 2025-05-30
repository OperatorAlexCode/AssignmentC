package com.example.assignmentc.ui.navigation

sealed class Screen(val route: String) {
    object Start : Screen("start")
    object Game : Screen("game")
    object Leaderboard : Screen("leaderboard")
    object MazePicker : Screen("mazePicker")
}