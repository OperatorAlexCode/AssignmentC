package com.example.assignmentc.logic

import android.content.Context
import android.media.MediaPlayer
import com.example.assignmentc.R

class DollarBillItem(override var context: Context,override var tile: Tile) : Item(context,tile) {
    override val isPlaced: Boolean get() = true
    override fun spriteRes(): Int = R.drawable.dollar_bill
    //override var pickupSfx: MediaPlayer = MediaPlayer.create(context,R.raw.scoreup)

    override fun onTrigger(gameManager: GameManager, triggeringEnemy: Enemy) {
    }

    override fun place(on: Tile) {
    }

    override fun onPlayerPickup(gameManager: GameManager) {
        //pickupSfx.start()
        gameManager.increaseScore(5)
    }
}
