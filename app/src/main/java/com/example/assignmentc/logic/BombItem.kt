package com.example.assignmentc.logic

import android.content.Context
import androidx.compose.ui.graphics.asImageBitmap
import com.example.assignmentc.R

class BombItem(
    override var tile: Tile,
    private val context: Context
) : Item(tile) {

    private var placed    = false
    override val isPlaced: Boolean get() = placed

    private var primed    = false
    private var exploding = false

    // ⚙️ Use the right parameter names: rows and columns
    private val primeAnimator     =
        Animator(context, R.drawable.bomb_anim, rows = 2, columns = 2)
    private val explosionAnimator =
        Animator(context, R.drawable.explosion, rows = 1, columns = 3)

    override fun spriteRes(): Int = R.drawable.bomb

    override fun place(on: Tile) {
        placed = true
        primed = true
        tile   = on
    }

    override fun onTrigger(gameManager: GameManager, triggeringEnemy: Enemy) {
        gameManager.EnemyManager.removeEnemy(triggeringEnemy)
        gameManager.increaseScore(15)
        primed    = false
        exploding = true
    }

    /** Returns true when explosion’s last frame has been shown */
    fun update(): Boolean = when {
        placed && primed -> {
            primeAnimator.Update()
            false
        }
        exploding -> {
            explosionAnimator.Update()
            // Compare against SheetColumns, not FrameCount
            explosionAnimator.CurrentFrame == explosionAnimator.SheetColumns - 1
        }
        else -> false
    }

    override fun getBitmap(context: Context) = when {
        !placed           -> super.getBitmap(context)
        placed && primed  -> primeAnimator.GetSprite().asImageBitmap()
        exploding         -> explosionAnimator.GetSprite().asImageBitmap()
        else              -> super.getBitmap(context)
    }
}
