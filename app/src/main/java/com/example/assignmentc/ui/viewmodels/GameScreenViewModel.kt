package com.example.assignmentc.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.assignmentc.logic.other.Direction
import com.example.assignmentc.logic.GameManager
import com.example.assignmentc.logic.other.Maze
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameScreenViewModel(
    application: Application,
    initialMaze: Maze
) : AndroidViewModel(application) {

    val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    private var _maze = mutableStateOf<Maze>(initialMaze)
    val maze: State<Maze> = _maze

    private val _playerHealth = mutableStateOf(3)
    val playerHealth: State<Int> = _playerHealth

    //private val playerManager = PlayerManager(application, _maze.value)
    //private val enemyManager = EnemyManager(application, _maze.value, playerManager)

    private val _isGameOver = mutableStateOf(false)
    val isGameOver: State<Boolean> = _isGameOver

    private val gameManager = GameManager(application, _maze.value).apply {
        onGameEnd = {
            _isGameOver.value = true
        }
    }

    //private val _hasHeldItem = mutableStateOf(gameManager.itemManager.heldItem != null)
    //val hasHeldItem: State<Boolean> = _hasHeldItem

    //private val _heldItemRes = mutableStateOf<Int?>(gameManager.itemManager.heldItem?.spriteRes())
    //val heldItemRes: State<Int?> = _heldItemRes

    init {
        //playerManager.spawnPlayer()
        //enemyManager.spawnEnemies()
        startGame()
    }

    fun startGame() {
        _isGameOver.value = false
        gameManager.StartGame(_maze.value)
        _playerHealth.value = gameManager.player?.health ?: 3
        scope.launch {
            while (!_isGameOver.value) {
                if (gameManager.effects.count() > 0)
                    UpdateMaze()

                delay(100)
            }
        }
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
        UpdateMaze()
        gameManager.Update()

        // Refresh held‐item state
        //_hasHeldItem.value = (gameManager.itemManager.heldItem != null)
        //_heldItemRes.value = gameManager.itemManager.heldItem?.spriteRes()
    }

    fun UpdateMaze() {
        _maze.value = _maze.value.copySelf()
        _playerHealth.value = gameManager.player?.health ?: 0
    }

    fun useItem() {
        // Delegate to GameManager, which calls ItemManager.useHeldItem(...)
        val didUse = gameManager.useHeldItem()
        if (didUse) {
            _maze.value = gameManager.currentMaze.copySelf()
        }
        // Refresh held‐item state
        //_hasHeldItem.value = (gameManager.itemManager.heldItem != null)
        //_heldItemRes.value = gameManager.itemManager.heldItem?.spriteRes()
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
