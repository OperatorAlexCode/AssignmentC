package com.example.assignmentc

class Enemy(var currentTile: Tile) {

    fun move() {
        val directions = TileDirection.entries.shuffled()

        for (direction in directions) {
            val nextTile = currentTile.GetTile(direction)
            if (nextTile != null && !nextTile.IsWall) {
                currentTile = nextTile
                break
            }
        }
    }

    fun getLocationTile(): Tile {
        return currentTile
    }
}