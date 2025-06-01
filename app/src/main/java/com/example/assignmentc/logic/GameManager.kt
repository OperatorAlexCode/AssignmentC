package com.example.assignmentc.logic

import android.content.Context

class GameManager(var context: Context, private var maze: Maze) {
    var player: Player? = null
    var EnemyManager = EnemyManager(context,maze,this)

    var score: Int = 0
    private var playerMoveCount: Int = 0
    val maxAmountEnemies = 10

    var onGameEnd: (() -> Unit)? = null

    fun StartGame() {
        spawnPlayer()
        EnemyManager = EnemyManager(context,maze,this)
        EnemyManager.spawnEnemies(3)
    }

    fun StartGame(maze: Maze) {
        this.maze = maze
        StartGame()
    }

    fun Update() {
        player?.health?.let {
            if (it <= 0)
            {
                EndGame()
                return
            }
        }

        EnemyManager.moveAllEnemies()
    }

    fun EndGame() {
        onGameEnd?.invoke()
    }

    fun spawnPlayer() {
        //val nonWallTiles = maze.Tiles.flatten().filter { !it.IsWall }
        //locationTile = maze.Tiles[0][0]
        player = Player(context,maze.Tiles[maze.PlayerStart.XPos][maze.PlayerStart.YPos])
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

        playerMoveCount++
        if (playerMoveCount >= 5) { //5 may be changed for balancing purposes
            if (EnemyManager.enemies.size < maxAmountEnemies) {
                EnemyManager.spawnEnemies(1)
                playerMoveCount = 0
            }
        }
    }

    fun getPlayerLocation() : Tile? {
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
}