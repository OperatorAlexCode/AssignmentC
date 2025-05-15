package com.example.assignmentc

class Maze {

    val Height: Int = 11
    val Width: Int = 11
    var Tiles: Array<Array<Tile>> = emptyArray()

    init {
        Construct()
    }

    // Constructs the maze using a given instruction
    fun Construct() {
        Tiles = Array(Height) { emptyArray() }

        for (y in 0..Height-1){
            var row = emptyArray<Tile>()

            for (x in 0..Width-1){
                row += Tile(x,y,false)
            }

            Tiles[y] = row
        }
    }
}