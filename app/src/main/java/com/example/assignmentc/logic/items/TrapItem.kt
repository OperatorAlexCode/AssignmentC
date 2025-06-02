package com.example.assignmentc.logic.items

import android.content.Context
import android.media.MediaPlayer
import com.example.assignmentc.R
import com.example.assignmentc.logic.playerandenemies.Enemy
import com.example.assignmentc.logic.GameManager
import com.example.assignmentc.logic.other.Tile

class TrapItem(
    override var context: Context,
    override var tile: Tile
) : Item(context,tile) {
    private var placed: Boolean = false
    override val isPlaced: Boolean get() = placed

    val useSfx: MediaPlayer = MediaPlayer.create(context,R.raw.place)
    val activateSfx: MediaPlayer = MediaPlayer.create(context,R.raw.slip)

    // Draw banana on ground
    override fun spriteRes(): Int =
        if (!placed) R.drawable.banana else R.drawable.banana_peel

    override fun onTrigger(gameManager: GameManager, triggeringEnemy: Enemy) {
        activateSfx.start()
        gameManager.EnemyManager.removeEnemy(triggeringEnemy)
        gameManager.increaseScore(10)
        gameManager.itemManager.remove(this)
    }


    override fun place(on: Tile) {
        tile = on
        placed = true
        useSfx.start()
    }
}
