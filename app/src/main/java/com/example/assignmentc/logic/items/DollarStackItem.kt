package com.example.assignmentc.logic.items

import android.content.Context
import com.example.assignmentc.R
import com.example.assignmentc.logic.playerandenemies.Enemy
import com.example.assignmentc.logic.GameManager
import com.example.assignmentc.logic.other.Tile

class DollarStackItem(override var context: Context,override var tile: Tile) : Item(context,tile) {
    override val isPlaced: Boolean get() = true
    override fun spriteRes(): Int = R.drawable.dollar_stack
    //override var pickupSfx: MediaPlayer = MediaPlayer.create(context,R.raw.scoreUp)

    override fun onTrigger(gameManager: GameManager, triggeringEnemy: Enemy) { /* no‐op */ }
    override fun place(on: Tile) { /* no‐op */ }

    override fun onPlayerPickup(gameManager: GameManager) {
        //pickupSfx.start()
        gameManager.increaseScore(15)
    }
}
