package com.example.assignmentc.logic

import com.example.assignmentc.R

class TrapItem(
    override var tile: Tile
) : Item(tile) {
    private var placed: Boolean = false
    override val isPlaced: Boolean get() = placed

    // Draw banana on ground
    override fun spriteRes(): Int =
        if (!placed) R.drawable.banana else R.drawable.banana_peel

    override fun onTrigger(gameManager: GameManager, triggeringEnemy: Enemy) {
        gameManager.EnemyManager.removeEnemy(triggeringEnemy)
        gameManager.increaseScore(10)
        gameManager.itemManager.remove(this)
    }


    override fun place(on: Tile) {
        tile = on
        placed = true
    }
}
