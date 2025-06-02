package com.example.assignmentc.logic

import com.example.assignmentc.R

class DollarBillItem(override var tile: Tile) : Item(tile) {
    override val isPlaced: Boolean get() = true
    override fun spriteRes(): Int = R.drawable.dollar_bill

    override fun onTrigger(gameManager: GameManager, triggeringEnemy: Enemy) {
    }

    override fun place(on: Tile) {
    }

    override fun onPlayerPickup(gameManager: GameManager) {
        gameManager.increaseScore(5)
    }
}
