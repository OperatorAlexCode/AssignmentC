package com.example.assignmentc.logic

import android.content.Context
import com.example.assignmentc.logic.items.Item
import com.example.assignmentc.logic.items.ItemManager
import com.example.assignmentc.logic.items.TrapItem
import com.example.assignmentc.logic.other.Direction
import com.example.assignmentc.logic.other.Maze
import com.example.assignmentc.logic.other.Tile
import com.example.assignmentc.logic.playerandenemies.Enemy
import com.example.assignmentc.logic.playerandenemies.EnemyManager
import com.example.assignmentc.logic.playerandenemies.Player

class GameManager(var context: Context, private var maze: Maze) {
    var player: Player? = null
    var EnemyManager = EnemyManager(context, maze, this)


    val itemManager = ItemManager(maze, context, this)

    var score: Int = 0
    private var playerMoveCount: Int = 0
    var spawnInterval = 15
    val maxAmountEnemies = 10

    var onGameEnd: (() -> Unit)? = null

    fun StartGame() {
        score = 0
        spawnPlayer()
        EnemyManager = EnemyManager(context, maze, this)
        EnemyManager.spawnEnemies(1)
    }

    fun StartGame(maze: Maze) {
        this.maze = maze
        StartGame()
    }

    fun Update() {
        EnemyManager.moveAllEnemies()

        player?.health?.let {
            if (it <= 0)
            {
                EndGame()
                return
            }
        }

        score+=1

        // Changed here
        val enemiesSnapshot = EnemyManager.enemies.toList()
        enemiesSnapshot.forEach { enemy ->
            val eTile = enemy.getLocationTile()
            val trapsHere = itemManager.items
                .filterIsInstance<TrapItem>()
                .filter { it.tile == eTile && it.isPlaced }
            trapsHere.forEach { trap ->
                trap.onTrigger(this, enemy)
            }
        }

        itemManager.updateBombs()
        itemManager.onNewTurn()
    }

    fun EndGame() {
        onGameEnd?.invoke()
    }

    fun spawnPlayer() {
        //val nonWallTiles = maze.Tiles.flatten().filter { !it.IsWall }
        //locationTile = maze.Tiles[0][0]
        player = Player(context, maze.Tiles[maze.PlayerStart.XPos][maze.PlayerStart.YPos])
        player?.HealMax()
    }

    fun movePlayer(direction: Direction) {
        /*val nextTile = player?.currentTile?.GetTile(direction)
        if (nextTile != null && !nextTile.IsWall) {
            player?.currentTile?.isPlayerLocation = false
            nextTile.isPlayerLocation = true
            //player?.currentTile = nextTile
            locationTile = nextTile
        }*/

        val nextTile = player?.currentTile?.GetTile(direction)
        if (player?.currentTile?.GetTile(direction) != null) {
            //player?.currentTile?.isPlayerLocation = false
            //nextTile?.isPlayerLocation = true
            player?.currentTile = nextTile
        }

        player?.Update(direction)

        // Try picking up any ground item on the new tile:
        player?.currentTile?.let { itemManager.tryPickUp(it) }

        playerMoveCount++
        if (playerMoveCount >= spawnInterval) { //5 may be changed for balancing purposes
            if (EnemyManager.enemies.size < maxAmountEnemies) {
                EnemyManager.spawnEnemies(1)
                playerMoveCount = 0
            }
        }
    }

    fun useHeldItem(): Boolean {
        val pTile = player?.currentTile ?: return false
        return itemManager.useHeldItem(pTile)
    }

    val currentMaze: Maze
        get() = maze

    fun getPlayerLocation(): Tile? {
        return player?.currentTile
    }

    fun isPlayerOnTile(x:Int,y:Int): Boolean {
        var output = false

        player?.let {
            output = it.IsOnTile(x,y)
        }

        return output
    }

    fun isEnemyOnTile(x:Int,y:Int):Boolean {
        return EnemyManager.enemies.any { e -> e.isOnTile(x,y) }
    }

    fun getEnemyOnTile(x:Int,y:Int): Enemy? {
        return EnemyManager.enemies.find { e -> e.isOnTile(x,y) }
    }

    fun increaseScore(gainedScore:Int) {
        score += gainedScore
    }

    fun damagePlayer(amount: Int) {
        player?.Hurt(amount)
    }

    fun getHeldItem(): Item? {
        return itemManager.heldItem
    }
}