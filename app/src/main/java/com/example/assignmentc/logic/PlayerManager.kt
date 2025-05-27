package com.example.assignmentc.logic

import android.content.Context


class PlayerManager(var context: Context, private val maze: Maze) {
    private val trapManager = TrapManager(maze)

    var player: Player? = null
    //var locationTile: Tile? = null
    var score: Int = 0

    fun spawnPlayer() {
        //val nonWallTiles = maze.Tiles.flatten().filter { !it.IsWall }
        //locationTile = maze.Tiles[0][0]
        player = Player(context,maze.Tiles[0][0])
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
            player?.currentTile?.isPlayerLocation = false
            nextTile?.isPlayerLocation = true
            player?.currentTile = nextTile
        }

        player?.Update(direction)
        player?.currentTile?.let { trapManager.tryPickUp(it) }
    }

    // true if the player currently holds a trap
    fun canDropTrap(): Boolean {
        return trapManager.isHeld()
    }

    // Drop the trap on the current tile if the player is holding one.
    fun dropTrap() {
        player?.currentTile?.let { trapManager.drop(it) }
    }

    // Advance all trap timers by one turn.
    fun tickTurn() {
        trapManager.tickAll()
    }

    fun getPlayerLocation() : Tile? {
        return player?.currentTile
    }

    fun isOnTile(x:Int,y:Int): Boolean {
        var output = false

        player?.let {
            output = it.IsOnTile(x,y)
        }

        return output
    }
}