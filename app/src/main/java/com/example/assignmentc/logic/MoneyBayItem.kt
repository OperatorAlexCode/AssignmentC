package com.example.assignmentc.logic

import com.example.assignmentc.R

class MoneyBagItem(override var tile: Tile) : Item(tile) {
    override val isPlaced: Boolean get() = true
    override fun spriteRes(): Int = R.drawable.money_bag

    override fun onTrigger(gameManager: GameManager, triggeringEnemy: Enemy) { /* no‐op */ }
    override fun place(on: Tile) { /* no‐op */ }

    override fun onPlayerPickup(gameManager: GameManager) {
        gameManager.increaseScore(30)
    }
}
