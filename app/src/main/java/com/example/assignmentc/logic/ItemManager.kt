package com.example.assignmentc.logic

import android.content.Context

class ItemManager(
    private val maze: Maze,
    private val context: Context,
    private val gameManager: GameManager
) {

    // All the items (both pickup‐state and placed)
    internal val _items = mutableListOf<Item>()
    val items: List<Item> get() = _items

    var heldItem: Item? = null

    private val trapSpawnIntervalTurns = 5    // spawn a new banana‐trap every 5 turns
    private var turnsSinceLastTrap = 0

    // Added here: separate counter for bombs
    private val bombSpawnIntervalTurns = 4   // spawn a new bomb every 5 turns
    private var turnsSinceLastBomb = 0

    // Optional cap of banana, for testing purposes
    private val maxGroundTraps = 4

    fun tryPickUp(tile: Tile): Boolean {
        val pickup = findPickupOn(tile) ?: return false
        // 1) Drop old held item (remove from world)
        heldItem?.let { remove(it) }
        // 2) Grab the new one
        heldItem = pickup
        // 3) Remove it from the ground
        remove(pickup)
        return true
    }

    fun useHeldItem(on: Tile): Boolean {
        val item = heldItem ?: return false
        item.place(on)
        _items += item
        heldItem = null
        return true
    }

    fun updateBombs(): Int {
        val toRemove = mutableListOf<BombItem>()
        for (item in _items) {
            if (item is BombItem) {
                // Before calling update(), check if the bomb is about to enter its “exploding” state
                val wasPrimed = item.isPrimed
                val finished = item.update()

                if (wasPrimed && item.isExploding) {
                    // 1) Compute all tiles within blast radius
                    val blastTiles = item.getBlastTiles(maze)

                    // 2) For every enemy still alive on one of those tiles, remove and award points
                    val enemiesToKill = gameManager.EnemyManager.enemies
                        .filter { e -> blastTiles.contains(e.currentTile) }
                    for (enemy in enemiesToKill) {
                        gameManager.EnemyManager.removeEnemy(enemy)
                        // Award points for each enemy caught in blast
                        gameManager.increaseScore(20)
                    }
                }

                if (finished) {
                    toRemove += item
                }
            }
        }
        toRemove.forEach { remove(it) }
        return toRemove.size
    }

    // Spawn a new TrapItem lying on the ground at tile T
    fun spawnTrap(at: Tile) {
        _items += TrapItem(at)
    }

    // Spawn a new BombItem lying on the ground at tile T
    fun spawnBomb(at: Tile) {
        _items += BombItem(at, context)
    }

    // Remove an item when it’s picked up or triggered
    fun remove(item: Item) {
        _items.remove(item)
    }

    // Find the first “pickup” (not yet placed) item on this tile
    fun findPickupOn(tile: Tile): Item? =
        _items.firstOrNull { it.tile == tile && !it.isPlaced }

    fun onNewTurn() {
        turnsSinceLastTrap++
        turnsSinceLastBomb++

        // Only spawn if enough turns have passed AND we’re under the cap
        val groundTrapCount = _items.count { it is TrapItem && !it.isPlaced }
        if (turnsSinceLastTrap >= trapSpawnIntervalTurns && groundTrapCount < maxGroundTraps) {
            // Choose a random valid tile (we’ll put chooseSpawnTile() below)
            val spawnTile = chooseSpawnTile()
            spawnTrap(spawnTile)
            turnsSinceLastTrap = 0  // reset the trap counter
        }

        // spawn a bomb every bombSpawnIntervalTurns
        if (turnsSinceLastBomb >= bombSpawnIntervalTurns) {
            val bombTile = chooseSpawnTile()  // reuse the same helper
            spawnBomb(bombTile)
            turnsSinceLastBomb = 0  // reset the bomb counter
        }
    }

    private fun chooseSpawnTile(): Tile {
        val size = maze.Size
        val min = size / 4
        val max = size * 3 / 4

        val candidates = maze.Tiles.flatten()
            .filter { !it.IsWall }
            .filter { it.XPos in min..max && it.YPos in min..max } // central quadrant
        return candidates.shuffled().first()
    }
}
