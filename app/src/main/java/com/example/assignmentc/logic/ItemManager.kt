package com.example.assignmentc.logic

import android.content.Context

class ItemManager(private val maze: Maze, private val context: Context) {

    // All the items (both pickup-state and placed)
    internal val _items = mutableListOf<Item>()
    val items: List<Item> get() = _items

    var heldItem: Item? = null           // ← Newly added

    /**
     * If there is a “pickup” (isPlaced == false) at `tile`, then:
     *   1) Remove any previously held item (drop it entirely).
     *   2) Set that ground item into heldItem.
     *   3) Remove it from the world list (_items).
     * Returns true if an item was picked up, false otherwise.
     */
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

    /**
     * If the player is holding an item, place it onto `tile`, re‐insert into _items,
     * clear heldItem, and return true. Otherwise return false.
     *
     * After placing:
     *   - For TrapItem: tile→ peel sprite.
     *   - For BombItem: tile→ start priming.
     */
    fun useHeldItem(on: Tile): Boolean {
        val item = heldItem ?: return false
        item.place(on)          // banana→peel or bomb→primed
        _items += item          // put it back into the world
        heldItem = null
        return true
    }

    /**
     * Advance any BombItem animations by one “tick.” If update() returns true (meaning the final
     * explosion frame has just been shown), remove that bomb from _items.
     * Return the number of bombs that finished exploding this tick (or a list if you prefer).
     */
    fun updateBombs(): Int {
        val toRemove = mutableListOf<BombItem>()
        for (item in _items) {
            if (item is BombItem) {
                val finished = item.update()
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

    // Delay
}
