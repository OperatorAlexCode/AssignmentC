package com.example.assignmentc.logic

import com.example.assignmentc.R
import com.example.assignmentc.logic.Item
import com.example.assignmentc.logic.Enemy
import com.example.assignmentc.logic.GameManager
import com.example.assignmentc.logic.Tile

class TrapItem(
    override var tile: Tile
) : Item(tile) {
    /** Flip to true once the player “plants” the trap. */
    private var placed: Boolean = false
    override val isPlaced: Boolean get() = placed

    /** Draw banana on ground, peel when placed. */
    override fun spriteRes(): Int =
        if (!placed) R.drawable.banana else R.drawable.banana_peel

    /**
     * Called when an enemy walks on a placed peel:
     * Removes the enemy via your EnemyManager and awards score.
     */
    override fun onTrigger(gameManager: GameManager, triggeringEnemy: Enemy) {
        // Remove this enemy from the game
        gameManager.EnemyManager.removeEnemy(triggeringEnemy)
        // Award points for a successful trap
        gameManager.increaseScore(10)
    }

    /** Helper for when the player “uses” the picked-up trap */
    fun place(on: Tile) {
        tile = on
        placed = true
    }
}
