package com.example.assignmentc.logic.items

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.assignmentc.logic.playerandenemies.Enemy
import com.example.assignmentc.logic.GameManager
import com.example.assignmentc.logic.other.Tile

sealed class Item(open var context: Context, open var tile: Tile) {

    // Has the player already placed this item (vs. it lying on the ground)?
    abstract val isPlaced: Boolean

    // Which sprite to draw for the current state?
    abstract fun spriteRes(): Int

    /**
     * Default bitmap fetcher: loads the spriteRes() drawable.
     * Subclasses with animations can override this.
     */
    open fun getBitmap(context: Context): ImageBitmap {
        val bmp = BitmapFactory.decodeResource(context.resources, spriteRes())
        return bmp.asImageBitmap()
    }

    /**
     * Place this item on a tile (default: no-op).
     * Subclasses override to flip their own flags.
     */
    open fun place(on: Tile) {
        // default: do nothing
    }

    /**
     * Called when an Enemy steps onto this item _after_ it’s been placed.
     * Implementations should apply effects (kill(), stun(), etc.) and
     * queue themselves for removal from ItemManager.
     */
    abstract fun onTrigger(gameManager: GameManager, triggeringEnemy: Enemy)

    /**
     * Called when the player steps onto this item _before_ it’s been placed or
     * if it’s a score‐only pickup on the ground. Default is no-op.
     * Subclasses override to award points or apply other effects immediately.
     */
    open fun onPlayerPickup(gameManager: GameManager) {
        // default: do nothing
    }
}
