package com.example.assignmentc.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.assignmentc.logic.Direction
import com.example.assignmentc.logic.EnemyManager
import com.example.assignmentc.logic.Maze
import com.example.assignmentc.logic.PlayerManager
import com.example.assignmentc.logic.TempleMaze

class GameScreenViewModel(application: Application) : AndroidViewModel(application) {

    private var _maze = mutableStateOf<Maze>(TempleMaze())
    val maze: State<Maze> = _maze

    private val playerManager = PlayerManager(application, _maze.value)
    private val enemyManager = EnemyManager(application, _maze.value, playerManager)

    init {
        playerManager.spawnPlayer()
        enemyManager.spawnEnemies()
    }

    fun movePlayer(direction: Direction) {
        playerManager.movePlayer(direction)
        _maze.value = _maze.value.copySelf()
        val playerTile = playerManager.player?.currentTile
        if (playerTile != null) {
            enemyManager.moveAllEnemies()
        }
    }

    fun getPlayerManager(): PlayerManager = playerManager
    fun getEnemyManager(): EnemyManager = enemyManager
}

class GameScreenViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameScreenViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
