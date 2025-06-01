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

    // Separate counter for bombs
    private val bombSpawnIntervalTurns = 10   // spawn a new bomb every 10 turns
    private var turnsSinceLastBomb = 0

    // Counter for score‐only pickups
    private val coinSpawnIntervalTurns = 7    // spawn a coin every 3 turns
    private var turnsSinceLastCoin = 0

    // Optional cap of banana traps, for testing purposes
    private val maxGroundTraps = 4

    /**
     * Attempt to pick up any item on `tile`. Priority:
     *   1) If there is a “score‐only” item (DollarBillItem, DollarStackItem, MoneyBagItem),
     *      immediately call onPlayerPickup(...) and remove it from the world.
     *   2) Otherwise, if there is an un‐placed item (trap or bomb), pick it up into heldItem.
     *
     * Returns true if any item was picked up (or collected), false otherwise.
     */
    fun tryPickUp(tile: Tile): Boolean {
        // 1) Look for score‐only items first (ignore isPlaced flag)
        val scorePickup = _items.firstOrNull {
            it.tile == tile && (it is DollarBillItem || it is DollarStackItem || it is MoneyBagItem)
        }
        if (scorePickup != null) {
            // Award points (onPlayerPickup) and remove from the world
            scorePickup.onPlayerPickup(gameManager)
            remove(scorePickup)
            return true
        }

        // 2) Otherwise, look for an un‐placed item (trap or bomb) at this tile
        val pickup = _items.firstOrNull { it.tile == tile && !it.isPlaced } ?: return false
        // Drop old held item (if any), then hold this new one
        heldItem?.let { remove(it) }
        heldItem = pickup
        remove(pickup)
        return true
    }

    /**
     * If the player is holding an item (trap or bomb), place it onto `tile`,
     * put it back into `_items`, and clear `heldItem`. Return true on success.
     * Otherwise return false.
     */
    fun useHeldItem(on: Tile): Boolean {
        val item = heldItem ?: return false
        item.place(on)
        _items += item
        heldItem = null
        return true
    }

    /**
     * Advance any BombItem animations by one “tick.” If update() returns true
     * (meaning the final explosion frame has just been shown), remove that bomb from _items.
     *
     * We also need to detect when the bomb first flips into `exploding == true`
     * so we can immediately damage any enemies in its blast radius (and award points).
     */
    fun updateBombs(): Int {
        val toRemove = mutableListOf<BombItem>()
        for (item in _items) {
            if (item is BombItem) {
                // Before calling update(), check if the bomb is about to enter its “exploding” state
                val wasPrimed = item.isPrimed
                val finished = item.update()

                // If we just transitioned from primed→exploding, burst the bomb:
                if (wasPrimed && item.isExploding) {
                    // 1) Compute all tiles within blast radius
                    val blastTiles = item.getBlastTiles(maze)

                    // 2) Remove any enemy in range, award points
                    val enemiesToKill = gameManager.EnemyManager.enemies
                        .filter { e -> blastTiles.contains(e.currentTile) }
                    for (enemy in enemiesToKill) {
                        gameManager.EnemyManager.removeEnemy(enemy)
                        gameManager.increaseScore(20)  // 20 points per enemy
                    }
                }

                // If the explosion animation is now fully finished, schedule removal
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
    private fun findPickupOn(tile: Tile): Item? =
        _items.firstOrNull { it.tile == tile && !it.isPlaced }

    /**
     * Called once per game turn. Advances all three counters (for traps, bombs, and coins)
     * and spawns each type when its interval is reached:
     *  - Traps every `trapSpawnIntervalTurns` (up to `maxGroundTraps`)
     *  - Bombs every `bombSpawnIntervalTurns`
     *  - Score‐items (“coins”) every `coinSpawnIntervalTurns`
     */
    fun onNewTurn() {
        turnsSinceLastTrap++
        turnsSinceLastBomb++
        turnsSinceLastCoin++

        // 1) Trap logic (every 5 turns, up to cap)
        val groundTrapCount = _items.count { it is TrapItem && !it.isPlaced }
        if (turnsSinceLastTrap >= trapSpawnIntervalTurns && groundTrapCount < maxGroundTraps) {
            val spawnTile = chooseSpawnTile()
            spawnTrap(spawnTile)
            turnsSinceLastTrap = 0
        }

        // 2) Bomb logic (every 10 turns)
        if (turnsSinceLastBomb >= bombSpawnIntervalTurns) {
            val bombTile = chooseSpawnTile()
            spawnBomb(bombTile)
            turnsSinceLastBomb = 0
        }

        // 3) Coin logic (every 3 turns)
        if (turnsSinceLastCoin >= coinSpawnIntervalTurns) {
            val spawnTile = chooseSpawnTile()
            when ((1..3).random()) {
                1 -> _items += DollarBillItem(spawnTile)
                2 -> _items += DollarStackItem(spawnTile)
                else -> _items += MoneyBagItem(spawnTile)
            }
            turnsSinceLastCoin = 0
        }
    }

    private fun chooseSpawnTile(): Tile {
        val size = maze.Size
        val min = size / 4
        val max = size * 3 / 4

        return maze.Tiles.flatten()
            .filter { !it.IsWall }                    // no walls
            .filter { it.XPos in min..max && it.YPos in min..max } // central quadrant
            .shuffled()
            .first()
    }
}
