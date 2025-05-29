package com.example.assignmentc.logic

class ItemManager(private val maze: Maze) {
    // All the items (both pickup-state and placed)
    internal val _items = mutableListOf<Item>()
    val items: List<Item> get() = _items

    /** Spawn a new TrapItem lying on the ground at tile T */
    fun spawnTrap(at: Tile) {
        _items += TrapItem(at)
    }

    /** Remove an item when it’s picked up or triggered */
    fun remove(item: Item) {
        _items.remove(item)
    }

    /** Find the first “pickup” (not yet placed) item on this tile */
    fun findPickupOn(tile: Tile): Item? =
        _items.firstOrNull { it.tile == tile && !it.isPlaced }
}
