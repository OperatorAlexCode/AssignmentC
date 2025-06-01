package com.example.assignmentc.logic

class Tile {
    val XPos: Int
    val YPos: Int
    val IsWall: Boolean
    //var isPlayerLocation: Boolean

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
        //isPlayerLocation = false
    }

    fun GetTile(tileToGet:Direction):Tile? {
        when (tileToGet) {
            Direction.North -> return NorthTile
            Direction.South -> return SouthTile
            Direction.East -> return EastTile
            Direction.West -> return WestTile
        }
    }

    fun directionTo(other: Tile): Direction? {
        return when {
            NorthTile == other -> Direction.North
            SouthTile == other -> Direction.South
            EastTile == other -> Direction.East
            WestTile == other -> Direction.West
            else -> null
        }
    }

}

enum class Direction {
    North,
    South,
    East,
    West
}