package com.example.assignmentc.logic.items

import android.content.Context
import android.media.MediaPlayer
import com.example.assignmentc.R
import com.example.assignmentc.logic.GameManager
import com.example.assignmentc.logic.other.Maze
import com.example.assignmentc.logic.other.Tile

class ItemManager(
    private val maze: Maze,
    private val context: Context,
    private val gameManager: GameManager
) {

    // All the items (both pickup‐state and placed)
    internal val _items = mutableListOf<Item>()
    val items: List<Item> get() = _items

    var heldItem: Item? = null

    /*private val trapSpawnIntervalTurns = 7    // spawn a new banana‐trap every 5 turns
    private var turnsSinceLastTrap = 0

    // Separate counter for bombs
    private val bombSpawnIntervalTurns = 7   // spawn a new bomb every 10 turns
    private var turnsSinceLastBomb = 0

    // Counter for score‐only pickups
    private val coinSpawnIntervalTurns = 5    // spawn a coin every 3 turns
    private var turnsSinceLastCoin = 0*/

    val spawnInterval = 7
    var currentInterval = 0

    // Optional cap of banana traps, for testing purposes
    private val maxGroundTraps = 4

    val pickupSfx: MediaPlayer = MediaPlayer.create(context,R.raw.slip)
    val scoreSfx: MediaPlayer = MediaPlayer.create(context,R.raw.scoreup)

    val enemyKillReward: Int = 30

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
        val instantUsePickup = _items.firstOrNull {
            it.tile == tile && (it is DollarBillItem || it is DollarStackItem || it is MoneyBagItem || it is MedKit)
        }
        if (instantUsePickup != null) {
            // Award points and remove
            instantUsePickup.onPlayerPickup(gameManager)
            remove(instantUsePickup)
            if (!(instantUsePickup is MedKit))
                scoreSfx.start()
            return true
        }

        // 2) Otherwise, look for an un‐placed item (trap or bomb) at this tile
        val pickup = _items.firstOrNull { it.tile == tile && !it.isPlaced } ?: return false
        heldItem?.let { remove(it) }
        heldItem = pickup
        remove(pickup)
        pickupSfx.start()
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
                val wasPrimed = item.isPrimed
                val finished = item.update()

                if (wasPrimed && item.isExploding) {
                    // Compute blast radius tiles
                    val blastTiles = item.getBlastTiles(maze)
                    // Remove any enemy in range, award points
                    val enemiesToKill = gameManager.EnemyManager.enemies
                        .filter { e -> blastTiles.contains(e.currentTile) }
                    for (enemy in enemiesToKill) {
                        gameManager.EnemyManager.removeEnemy(enemy)
                        gameManager.increaseScore(enemyKillReward)
                    }

                    for (tile in blastTiles)
                        gameManager.addEffect(Explosion(context,tile,2f))

                    toRemove += item
                }

                // If explosion animation is complete, schedule removal
                /*if (finished) {
                    toRemove += item
                }*/
            }
        }

        toRemove.forEach { remove(it) }
        return toRemove.size
    }

    // Spawn a new TrapItem lying on the ground at tile T
    fun spawnTrap(at: Tile) {
        _items += TrapItem(context,at)
    }

    // Spawn a new BombItem lying on the ground at tile T
    fun spawnBomb(at: Tile) {
        _items += BombItem(context,at)
    }

    fun spawnMedkit(at: Tile) {
        _items += MedKit(context,at)
    }

    // Remove an item when it’s picked up or triggered
    fun remove(item: Item) {
        _items.remove(item)
    }

    /**
     * Called once per game turn. Advances all three counters (for traps, bombs, and coins)
     * and spawns each type when its interval is reached:
     *  - Traps every `trapSpawnIntervalTurns` (up to `maxGroundTraps`)
     *  - Bombs every `bombSpawnIntervalTurns`
     *  - Score‐items (“coins”) every `coinSpawnIntervalTurns`
     */
    fun onNewTurn() {
        //turnsSinceLastTrap++
        //turnsSinceLastBomb++
        //turnsSinceLastCoin++

        // (1) Trap logic (every 5 turns, up to cap)
        /*val groundTrapCount = _items.count { it is TrapItem && !it.isPlaced }
        if (turnsSinceLastTrap >= trapSpawnIntervalTurns && groundTrapCount < maxGroundTraps) {
            chooseSpawnTile()?.let { spawnTrap(it) }
            turnsSinceLastTrap = 0
        }

        // (2) Bomb logic (every 10 turns)
        if (turnsSinceLastBomb >= bombSpawnIntervalTurns) {
            chooseSpawnTile()?.let { spawnBomb(it) }
            turnsSinceLastBomb = 0
        }

        // (3) Coin logic (every 3 turns)
        if (turnsSinceLastCoin >= coinSpawnIntervalTurns) {
            chooseSpawnTile()?.let { spawnTile ->
                when ((1..3).random()) {
                    1 -> _items += DollarBillItem(context,spawnTile)
                    2 -> _items += DollarStackItem(context,spawnTile)
                    else -> _items += MoneyBagItem(context,spawnTile)
                }
            }
            turnsSinceLastCoin = 0
        }*/

        if (currentInterval++ >= spawnInterval) {

            chooseSpawnTile()?.let { spawnTile ->

                if ((1..10).random() <= 6)
                    /*when ((1..3).random()) {
                        1 -> _items += DollarBillItem(context,spawnTile)
                        2 -> _items += DollarStackItem(context,spawnTile)
                        else -> _items += MoneyBagItem(context,spawnTile)
                    }*/
                    when ((1..6).random()) {
                        4 -> _items += DollarStackItem(context,spawnTile)
                        5 -> _items += DollarStackItem(context,spawnTile)
                        6 -> _items += MoneyBagItem(context,spawnTile)
                        else -> _items += DollarBillItem(context,spawnTile)
                    }

                else{
                    gameManager.player?.let {
                        if (it.health < it.maxHealth)
                            when ((1..3).random()) {
                                1 -> spawnTrap(spawnTile)
                                2 -> spawnBomb(spawnTile)
                                3 -> spawnMedkit(spawnTile)
                            }

                        else
                            when ((1..2).random()) {
                                1 -> spawnTrap(spawnTile)
                                2 -> spawnBomb(spawnTile)
                            }
                    }
                }
            }

            currentInterval = 0
        }
    }

    /**
     * Choose a random tile in the central region that:
     *  - is not a wall
     *  - is not already occupied by another Item (_items)
     * Returns null if no suitable tile is found.
     */
    private fun chooseSpawnTile(): Tile? {
        val size = maze.Size
        val min = size / 4
        val max = size * 3 / 4

        // Build a set of occupied Tile references
        val occupiedTiles = _items.map { it.tile }.toSet()

        // Filter to only valid, unoccupied tiles
        val candidates = maze.Tiles.flatten()
            .filter { !it.IsWall }
            .filter { it.XPos in min..max && it.YPos in min..max }
            .filter { !occupiedTiles.contains(it) }

        return if (candidates.isNotEmpty()) {
            candidates.shuffled().first()
        } else {
            null
        }
    }
}
