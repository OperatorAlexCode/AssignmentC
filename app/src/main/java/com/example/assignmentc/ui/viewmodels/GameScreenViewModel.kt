package com.example.assignmentc.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.assignmentc.logic.Direction
import com.example.assignmentc.logic.EnemyManager
import com.example.assignmentc.logic.GameManager
import com.example.assignmentc.logic.Maze
import com.example.assignmentc.logic.PlayerManager
import com.example.assignmentc.logic.TempleMaze

class GameScreenViewModel(application: Application) : AndroidViewModel(application) {

    private var _maze = mutableStateOf<Maze>(TempleMaze())
    val maze: State<Maze> = _maze


    //private val playerManager = PlayerManager(application, _maze.value)
    //private val enemyManager = EnemyManager(application, _maze.value, playerManager)

    private val gameManager = GameManager(application, _maze.value)
    private val _hasHeldItem = mutableStateOf(gameManager.heldItem != null)
    val hasHeldItem: State<Boolean> = _hasHeldItem


    // Tracks the drawable resource ID for the currently held item (or null)
    private val _heldItemRes = mutableStateOf<Int?>(gameManager.heldItem?.spriteRes())
    //val heldItemRes: State<Int?> = _heldItemRes


    init {
        //playerManager.spawnPlayer()
        //enemyManager.spawnEnemies()
        StartGame()
    }

    fun StartGame() {
        gameManager.StartGame(_maze.value)
        //_heldItemRes.value = gameManager.heldItem?.spriteRes()
    }

    fun StartGame(mazeToPlay: Maze) {
        _maze.value = mazeToPlay
        gameManager.StartGame(_maze.value)
        //_heldItemRes.value = gameManager.heldItem?.spriteRes()
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
        gameManager.Update(1000L)
        _hasHeldItem.value = (gameManager.heldItem != null)
        //_heldItemRes.value = gameManager.heldItem?.spriteRes()
    }

    fun useItem() {
        gameManager.useHeldItem()
        // Trigger a re-draw
        _maze.value = gameManager.currentMaze.copySelf()
        // update enabled state
        _hasHeldItem.value = (gameManager.heldItem != null)
        //_heldItemRes.value = gameManager.heldItem?.spriteRes()
    }

    val onUseItem = { useItem() }

    //fun getPlayerManager(): PlayerManager = playerManager
    //fun getEnemyManager(): EnemyManager = enemyManager
    fun getGameManager(): GameManager = gameManager
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
