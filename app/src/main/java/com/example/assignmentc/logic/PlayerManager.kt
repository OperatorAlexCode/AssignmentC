package com.example.assignmentc.logic

class PlayerManager(private val maze: Maze) {
    var player: Player? = null
    var locationTile: Tile? = null

    fun spawnPlayer() {
        val nonWallTiles = maze.Tiles.flatten().filter { !it.IsWall }
        locationTile = nonWallTiles.first()
        player = Player(locationTile)
    }

    fun movePlayer(direction: TileDirection) {
        val currentTile = player?.currentTile ?: return
        val nextTile = currentTile.GetTile(direction)
        if (nextTile != null && !nextTile.IsWall) {
            locationTile = nextTile
        }
    }

    fun getPlayerLocation() : Tile? {
        return locationTile
    }
}