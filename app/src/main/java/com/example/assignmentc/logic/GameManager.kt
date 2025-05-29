package com.example.assignmentc.logic

import android.content.Context

class  GameManager(var context: Context, private var maze: Maze) {
    var player: Player? = null
    var EnemyManager = EnemyManager(context,maze,this)
    val currentMaze: Maze
        get() = maze
    var score: Int = 0

    val itemManager = ItemManager(maze)

    // Added here ▶ Spawn timing & cap for items
    private val spawnIntervalMs = 5_000L   // Added here
    private var spawnAccumulatorMs = 0L     // Added here
    private val maxGroundItems = 5         // Added here

    var heldItem: Item? = null             // ← Don't forget this

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
        // 1) Health check
        player?.health?.takeIf { it <= 0 }?.also {
            EndGame()
            return
        }

        // 2) Spawn timer
        spawnAccumulatorMs += deltaTimeMs
        trySpawnItem()

        // 3) Enemy movement
        EnemyManager.moveAllEnemies()

        // 4) (Phase 3) Trigger detection will go here…
        for (enemy in EnemyManager.enemies.toList()) {
            val placedItem = itemManager.items
                .firstOrNull { it.tile == enemy.currentTile && it.isPlaced }
            if (placedItem != null) {
                placedItem.onTrigger(this, enemy)
                itemManager.remove(placedItem)
            }
        }
    }

    private fun trySpawnItem() {
        // Only spawn if enough time elapsed AND we’re under cap
        val groundCount = itemManager.items.count { !it.isPlaced }
        if (spawnAccumulatorMs >= spawnIntervalMs && groundCount < maxGroundItems) {
            val tile = chooseSpawnTile()
            itemManager.spawnTrap(tile)
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
     * Plants the trap at the player’s current tile and
     * moves it out of heldItem into itemManager’s placed list.
     */
    fun useHeldItem() {
        // Only TrapItems for now
        val item = heldItem as? TrapItem ?: return

        // Plant it under the player
        player?.currentTile?.let { item.place(it) }

        // Add it back into the world as a placed item
        itemManager._items += item     // or call a dedicated spawnPlaced()
        heldItem = null
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