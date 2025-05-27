package com.example.assignmentc.logic

import android.content.Context

class PlayerManager(var context: Context, private val maze: Maze) {
    var player: Player? = null
    //var locationTile: Tile? = null

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
    }

    fun getPlayerLocation() : Tile? {
        return player?.currentTile
    }

    fun isOnTile(x:Int,y:Int): Boolean {
        return player?.currentTile?.XPos == x && player?.currentTile?.YPos == y
    }
}