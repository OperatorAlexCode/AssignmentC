package com.example.assignmentc

class Maze {

    var Height: Int = 11
    var Width: Int = 11
    // 2D array of the tiles where:
    // 0,0 is the upper left corner,
    // Width-1, 0 is the upper right corner,
    // 0, Height-1 is the lower left corner and
    // Width-1, Height-1 is the lower right corner
    var Tiles: Array<Array<Tile>> = emptyArray()
    var player: Player? = null
    var enemies: MutableList<Enemy> = mutableListOf()

    constructor() {
        Construct()
        ConnectTiles()
        spawnPlayer()
        spawnEnemies()
    }

    constructor(height: Int, width:Int) {
        Height = height
        Width = width
        Construct()
        ConnectTiles()
    }

    // Default constructor that is used to create the maze using a given instruction
    fun Construct() {
        Tiles = Array(Height) { emptyArray() }

        for (x in 0..Width-1){
            var column = emptyArray<Tile>()

            for (y in 0..Height-1){
                column += Tile(x,y,if ((x == 0 || x == Width-1) || (y == 0 || y == Height-1) || (x % 2 == 0 && y % 2 == 0)) true else false)
            }

            Tiles[x] = column
        }
    }

    fun ConnectTiles() {
        for (x in 0..Width-1)
            for (y in 0.. Height-1) {
                if (y - 1 >= 0)
                    if (!Tiles[x][y-1].IsWall)
                        Tiles[x][y].NorthTile = Tiles[x][y-1]

                if (y + 1 < Height)
                    if (!Tiles[x][y+1].IsWall)
                        Tiles[x][y].SouthTile = Tiles[x][y+1]

                if (x - 1 >= 0)
                    if (!Tiles[x-1][y].IsWall)
                        Tiles[x][y].WestTile = Tiles[x-1][y]

                if (x + 1 < Width)
                    if (!Tiles[x+1][y].IsWall)
                        Tiles[x][y].EastTile = Tiles[x+1][y]
            }
    }

    fun spawnPlayer() {
        val nonWallTiles = Tiles.flatten().filter { !it.IsWall }
        val spawnTile = nonWallTiles.first()
        spawnTile.setPlayerLocation()
        player = Player(spawnTile)
    }

    fun spawnEnemies() {
        val nonWallOrPlayerTiles = Tiles.flatten().filter { !it.IsWall && !it.isPlayerLocation }
        val spawnTiles = nonWallOrPlayerTiles.shuffled().take(3)

        for (tile in spawnTiles) {
            enemies.add(Enemy(tile))
        }
    }

    fun moveEnemies() {
        for (enemy in enemies) {
            enemy.move()
        }
    }

    fun movePlayer(direction: TileDirection) {
        val nextTile = player?.currentTile?.GetTile(direction)
        if (nextTile != null && !nextTile.IsWall) {
            player?.currentTile?.isPlayerLocation = false
            nextTile.isPlayerLocation = true
            player?.currentTile = nextTile
        }
    }

    //function to force recomp
    fun copySelf(): Maze {
        return Maze(Height, Width).also {
            it.Tiles = this.Tiles
            it.player = this.player
            it.enemies = this.enemies
        }
    }

}