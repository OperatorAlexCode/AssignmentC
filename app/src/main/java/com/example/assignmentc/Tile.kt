package com.example.assignmentc

class Tile {
    val XPos: Int
    val YPos: Int
    val IsWall: Boolean
    var isPlayerLocation: Boolean

    // If tile isn't null then a walkable tile exist in the corresponding direction,
    // otherwise the tile is inaccessable or there is no tile
    var NorthTile: Tile? = null
    var SouthTile: Tile? = null
    var EastTile: Tile? = null
    var WestTile: Tile? = null

    constructor(xPos:Int, yPos:Int, isWall: Boolean) {
        XPos = xPos
        YPos = yPos
        IsWall = isWall
        isPlayerLocation = false
    }

    fun GetTile(tileToGet:TileDirection):Tile? {
        when (tileToGet) {
            TileDirection.North -> return NorthTile
            TileDirection.South -> return SouthTile
            TileDirection.East -> return EastTile
            TileDirection.West -> return WestTile
        }
    }

    fun setPlayerLocation() {
        if (isPlayerLocation) {
            isPlayerLocation = false
        } else {
            isPlayerLocation = true
        }
    }
}

enum class TileDirection {
    North,
    South,
    East,
    West
}