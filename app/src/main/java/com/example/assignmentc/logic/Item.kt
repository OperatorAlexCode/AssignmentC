package com.example.assignmentc.logic

import com.example.assignmentc.logic.Enemy
import com.example.assignmentc.logic.GameManager
import com.example.assignmentc.logic.Tile

sealed class Item(
    /** Which Tile this item currently “occupies” in the maze */
    open var tile: Tile
) {
    /** Has the player already placed this item (vs. it lying on the ground)? */
    abstract val isPlaced: Boolean

    /** Which sprite to draw for the current state? */
    abstract fun spriteRes(): Int

    /**
     * Called when an Enemy steps onto this item _after_ it’s been placed.
     * Implementations should apply effects (kill(), stun(), etc.) and
     * queue themselves for removal from ItemManager.
     */
    abstract fun onTrigger(gameManager: GameManager, triggeringEnemy: Enemy)
}
