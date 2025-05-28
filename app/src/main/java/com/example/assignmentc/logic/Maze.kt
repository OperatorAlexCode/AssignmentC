package com.example.assignmentc.logic

import kotlin.math.floor

open class Maze {
    var Size: Int = 13
    // 2D array of the tiles where:
    // 0,0 is the upper left corner,
    // Width-1, 0 is the upper right corner,
    // 0, Height-1 is the lower left corner and
    // Width-1, Height-1 is the lower right corner
    var Tiles: Array<Array<Tile>> = Array(Size) { emptyArray() }
    open var PlayerStart:Tile = Tile(Size/2,Size/2,false)

    constructor() {
        Construct()
        ConnectTiles()
    }

    constructor(size: Int) {
        Size = size
        Tiles = Array(Size) { emptyArray() }
        Construct()
        ConnectTiles()
    }

    constructor(toCopy: Maze) {
        Size = toCopy.Size
        Tiles = toCopy.Tiles
    }

    // Default constructor that is used to create the maze using a given instruction
    open fun Construct() {
        for (x in 0..<Size){
            var column = emptyArray<Tile>()

            for (y in 0..<Size){
                column += Tile(x,y,(x % 2 == 1 && y % 2 == 1))
            }

            Tiles[x] = column
        }
    }

    fun ConnectTiles() {
        for (x in 0..Size-1)
            for (y in 0.. Size-1) {
                if (y - 1 >= 0)
                    if (!Tiles[x][y-1].IsWall && Tiles[x][y].NorthTile == null)
                        Tiles[x][y].NorthTile = Tiles[x][y-1]

                if (y + 1 < Size)
                    if (!Tiles[x][y+1].IsWall && Tiles[x][y].SouthTile == null)
                        Tiles[x][y].SouthTile = Tiles[x][y+1]

                if (x - 1 >= 0)
                    if (!Tiles[x-1][y].IsWall && Tiles[x][y].WestTile == null)
                        Tiles[x][y].WestTile = Tiles[x-1][y]

                if (x + 1 < Size)
                    if (!Tiles[x+1][y].IsWall && Tiles[x][y].EastTile == null)
                        Tiles[x][y].EastTile = Tiles[x+1][y]
            }
    }

    //function to force recomp
    fun copySelf(): Maze {
        return Maze(this)
    }

}

class BlockMaze: Maze() {
    override fun Construct() {
        for (x in 0..<Size){
            var column = emptyArray<Tile>()

            for (y in 0..<Size){
                var check1 = x % 2 == 1
                var check2 = x % 4 == 2
                var check3 = y % 2 == 1
                var check4 = y % 4 == 2
                column += Tile(x,y,(check1 || check2) && (check3 || check4))
            }

            Tiles[x] = column
        }
    }
}

class LineMaze: Maze() {
    override fun Construct() {
        for (x in 0..<Size){
            var column = emptyArray<Tile>()

            for (y in 0..<Size){

                // x:2,y:3 -> true
                // x:6,y:3 -> false
                // x:10,y:3 -> true
                // x:2,y:5 -> false
                // x:6,y:5 -> true
                // x:10,y:5 -> false

                // x:3,y: -> true
                // x:3,y: -> false
                // x:3,y: -> true
                // x:3,y: -> false
                // x:3,y: -> true
                // x:3,y: -> false

                // x: 2,6,10 => 2+4*n
                // y:

                var check1 = x % 2 == 1
                var check2 = x % 4 == 2
                var check3 = y % 2 == 1
                var check4 = y % 4 == 2
                var check5 = x % 8 == 5
                var check6 = y % 8 == 1
                column += Tile(x,y,if ((check1 || (check2 && !check4 && !check5)) && (check3 || (check4 && !check2))) true else false)
            }

            Tiles[x] = column
        }
        for (y in 1..<Size step 4)
        {
            var state = y % 8 == 1

            for (x in 2..<Size step 4)
            {
                Tiles[x][y] = Tile(x,y,state)
                Tiles[x][y+2] = Tile(x,y,state)

                state = !state
            }
        }

        for (x in 1..<Size step 4)
        {
            var state = x % 8 == 5

            for (y in 2..<Size step 4)
            {
                Tiles[x][y] = Tile(x,y,state)
                Tiles[x+2][y] = Tile(x,y,state)

                state = !state
            }
        }
    }
}

class TempleMaze: Maze() {
    override fun Construct() {
        super.Construct()
        /*for (x in 0..<Width){
            var column = emptyArray<Tile>()

            // x:2->10,y:1 => true
            // x:2->10,y:2 => false
            // x:1,y:2 => true

            for (y in 0..<Height){
                var check1 = x % 2 == 1 && y % 2 == 1
                var check2 = x != 0 && x != Width-1
                var check3 = y != 0 && y != Width-1

                column += Tile(x,y,(check1 && check2 && check3))
            }

            Tiles[x] = column
        }*/

        var indent = -1

        for (x in 1..<Size step 2){
            if (x < Size/2)
                indent+=2

            var state = true
            var start = indent
            var end = Size-indent

            for (y in start..<end){
                if (y % 2 == 1 && y < end-1)
                {
                    Tiles[x][y] = Tile(x,y,state)
                    state = !state
                }

                else if (y == floor((Size/2).toFloat()).toInt())
                {
                    Tiles[x][y] = Tile(x,y,state)
                    state = !state
                }

                else
                    Tiles[x][y] = Tile(x,y,true)
            }

            if (x > Size/2)
                indent-=2
        }

        indent = -1

        for (y in 1..<Size step 2){
            if (y < Size/2)
                indent+=2

            var state = true
            var start = indent
            var end = Size-indent

            for (x in start..<end){
                if (x % 2 == 1 && x < end-1)
                {
                    Tiles[x][y] = Tile(x,y,state)
                    state = !state
                }

                else if (x == floor((Size/2).toFloat()).toInt())
                {
                    Tiles[x][y] = Tile(x,y,state)
                    state = !state
                }

                else
                    Tiles[x][y] = Tile(x,y,true)
            }

            if (y > Size/2)
                indent-=2
        }
    }
}