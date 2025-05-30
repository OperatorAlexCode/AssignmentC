package com.example.assignmentc.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.assignmentc.logic.Direction
import com.example.assignmentc.logic.GameManager
import com.example.assignmentc.logic.Maze

class GameScreenViewModel(
    application: Application,
    initialMaze: Maze
) : AndroidViewModel(application) {

    private var _maze = mutableStateOf<Maze>(initialMaze)
    val maze: State<Maze> = _maze

    //private val playerManager = PlayerManager(application, _maze.value)
    //private val enemyManager = EnemyManager(application, _maze.value, playerManager)

    private val gameManager = GameManager(application, _maze.value)

    init {
        //playerManager.spawnPlayer()
        //enemyManager.spawnEnemies()
        startGame()
    }

    fun startGame() {
        gameManager.StartGame(_maze.value)
    }

    /*fun EndGame() {
        gameManager.EndGame()
    }*/

    fun movePlayer(direction: Direction) {
        /*playerManager.movePlayer(direction)
        _maze.value = _maze.value.copySelf()
        val playerTile = playerManager.player?.currentTile
        if (playerTile != null) {
            enemyManager.moveAllEnemies()
        }*/

        gameManager.movePlayer(direction)
        _maze.value = _maze.value.copySelf()
        gameManager.Update()
    }

    //fun getPlayerManager(): PlayerManager = playerManager
    //fun getEnemyManager(): EnemyManager = enemyManager
    fun getGameManager(): GameManager = gameManager
}

class GameScreenViewModelFactory(
    private val application: Application,
    private val initialMaze: Maze
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameScreenViewModel(application, initialMaze) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
