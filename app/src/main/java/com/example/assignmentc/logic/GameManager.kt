package com.example.assignmentc.logic

import android.content.Context

class GameManager(var context: Context, private var maze: Maze) {
    var player: Player? = null
    var EnemyManager = EnemyManager(context,maze,this)

    var score: Int = 0

    fun StartGame() {
        spawnPlayer()
        EnemyManager = EnemyManager(context,maze,this)
        EnemyManager.spawnEnemies()
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
}