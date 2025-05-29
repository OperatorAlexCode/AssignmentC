package com.example.assignmentc.logic

import android.content.Context
import kotlin.random.Random

class  GameManager(var context: Context, private var maze: Maze) {
    var player: Player? = null
    var EnemyManager = EnemyManager(context,maze,this)
    val currentMaze: Maze
        get() = maze
    var score: Int = 0

    val itemManager = ItemManager(maze, context)

    // Spawn timing & cap for items
    private val spawnIntervalMs = 5_000L  // 5 seconds = 5 turns
    private var spawnAccumulatorMs = 0L
    private val maxGroundItems = 5

    var heldItem: Item? = null

    fun StartGame() {
        spawnPlayer()
        EnemyManager = EnemyManager(context,maze,this)
        EnemyManager.spawnEnemies()
    }

    fun StartGame(maze: Maze) {
        this.maze = maze
        StartGame()
    }

    fun Update(deltaTimeMs: Long) {
        player?.health?.takeIf { it <= 0 }?.also {
            EndGame()
            return
        }
        spawnAccumulatorMs += deltaTimeMs
        trySpawnItem()

        EnemyManager.moveAllEnemies()

        for (enemy in EnemyManager.enemies.toList()) {
            val placedItem = itemManager.items
                .firstOrNull { it.tile == enemy.currentTile && it.isPlaced }
            if (placedItem != null) {
                placedItem.onTrigger(this, enemy)
                itemManager.remove(placedItem)
            }
        }

        // tick all bombs and clean up finished explosions
        val toRemove = mutableListOf<Item>()
        itemManager.items.forEach { item ->
            if (item is BombItem && item.update()) {
                toRemove += item
            }
        }
        toRemove.forEach { itemManager.remove(it) }
    }

    private fun trySpawnItem() {
        // Only spawn if enough time elapsed AND we’re under cap
        val groundCount = itemManager.items.count { !it.isPlaced }
        if (spawnAccumulatorMs >= spawnIntervalMs && groundCount < maxGroundItems) {
            val tile = chooseSpawnTile()
            if (Random.nextBoolean()) {
                itemManager.spawnTrap(tile)
            } else {
                itemManager.spawnBomb(tile)
            }
            spawnAccumulatorMs = 0L
        }
    }

    private fun chooseSpawnTile(): Tile {
        // e.g. only tiles within 1/4–3/4 of maze size
        val size = maze.Size
        val min = size / 4
        val max = size * 3 / 4
        val candidates = maze.Tiles
            .flatten()
            .filter { !it.IsWall && player?.currentTile != it }
            .filter { it.XPos in min..max && it.YPos in min..max }
        return candidates.shuffled().first()
    }

    fun EndGame() {
    }

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
        if (nextTile != null) {
            player?.currentTile?.isPlayerLocation = false
            nextTile.isPlayerLocation = true
            player?.currentTile = nextTile
        }

        player?.Update(direction)

        // Pickup logic
        nextTile?.let { tile ->
            itemManager.findPickupOn(tile)?.let { item ->
                heldItem?.let { itemManager.remove(it) }
                heldItem = item
                itemManager.remove(item)
            }
        }
    }

    /**
     * Called by UI when the player “uses” their held item.
     * Plants the item at the player’s current tile and
     * moves it out of heldItem into itemManager’s placed list.
     */
    fun useHeldItem() {
        heldItem?.let { item ->
            player?.currentTile?.let { item.place(it) }
            itemManager._items += item
            heldItem = null
        }
    }

    fun getPlayerLocation() : Tile? {
        return player?.currentTile
    }

    fun isPlayerOnTile(x:Int,y:Int): Boolean {
        var output = false

        player?.let {
            output = it.IsOnTile(x,y)
        }

        return output
    }

    fun isEnemyOnTile(x:Int,y:Int):Boolean {
        return EnemyManager.enemies.any { e -> e.isOnTile(x,y) }
    }

    fun getEnemyOnTile(x:Int,y:Int): Enemy? {
        return EnemyManager.enemies.find { e -> e.isOnTile(x,y) }
    }

    fun increaseScore(gainedScore:Int) {
        score += gainedScore
    }
}