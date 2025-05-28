package com.example.assignmentc.logic

import android.content.Context

class PlayerManager(var context: Context, private val maze: Maze) {
    var player: Player? = null
    //var locationTile: Tile? = null
    var score: Int = 0

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
            player?.currentTile?.isPlayerLocation = false
            nextTile?.isPlayerLocation = true
            player?.currentTile = nextTile
        }

        player?.Update(direction)
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