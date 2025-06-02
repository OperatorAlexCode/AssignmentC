package com.example.assignmentc.logic.items

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.ui.graphics.asImageBitmap
import com.example.assignmentc.R
import com.example.assignmentc.logic.other.Animator
import com.example.assignmentc.logic.playerandenemies.Enemy
import com.example.assignmentc.logic.GameManager
import com.example.assignmentc.logic.other.Maze
import com.example.assignmentc.logic.other.Tile

class BombItem(
    override var context: Context,
    override var tile: Tile
) : Item(context,tile) {

    private var placed    = false
    override val isPlaced: Boolean get() = placed

    private var primed    = false
    val isPrimed: Boolean get() = primed

    private var exploding = false
    val isExploding: Boolean get() = exploding

    // countdown before explosion
    private var turnsUntilExplode = 3

    private val primeAnimator     =
        Animator(context, R.drawable.bomb_anim, rows = 2, columns = 2)
    private val explosionAnimator =
        Animator(context, R.drawable.explosion, rows = 1, columns = 3)

    val useSfx: MediaPlayer = MediaPlayer.create(context,R.raw.place)
    val activateSfx: MediaPlayer = MediaPlayer.create(context,R.raw.explosion)

    override fun spriteRes(): Int = R.drawable.bomb

    override fun place(on: Tile) {
        placed = true
        primed = true
        tile   = on
        turnsUntilExplode = 3
        useSfx.start()
    }

    override fun onTrigger(gameManager: GameManager, triggeringEnemy: Enemy) {
        // Force the bomb into “exploding” state immediately
        /*primed = false
        exploding = true
        // Award base points for stepping on it
        gameManager.increaseScore(15)*/
    }


    fun update(): Boolean {
        return when {
            !placed -> false

            // Still in fuse‐countdown
            primed -> {
                turnsUntilExplode--
                // Play fuse animation frame
                primeAnimator.Update()

                if (turnsUntilExplode <= 0) {
                    // Time to explode
                    primed = false
                    exploding = true
                    activateSfx.start()
                }

                false
            }

            exploding -> {
                // Play explosion animation frames
                explosionAnimator.Update()
                explosionAnimator.CurrentFrame == explosionAnimator.SheetColumns - 1
            }

            else -> false
        }
    }

    override fun getBitmap(context: Context) = when {
        !placed           -> super.getBitmap(context)
        placed && primed  -> primeAnimator.GetSprite().asImageBitmap()
        exploding         -> explosionAnimator.GetSprite().asImageBitmap()
        else              -> super.getBitmap(context)
    }


    fun getBlastTiles(maze: Maze): List<Tile> {
        val result = mutableListOf<Tile>()
        // Always include center
        result += tile

        // Helper to walk in one direction until either 2 steps, or a wall blocks:
        fun walkDir(dx: Int, dy: Int) {
            var steps = 0
            var current = tile
            while (steps < 2) {
                val next = when {
                    dx == -1 -> current.WestTile
                    dx == 1  -> current.EastTile
                    dy == -1 -> current.NorthTile
                    dy == 1  -> current.SouthTile
                    else     -> null
                }
                if (next == null || next.IsWall) break
                result += next
                current = next
                steps++
            }
        }

        // North, South, West, East
        walkDir(0, -1)
        walkDir(0, 1)
        walkDir(-1, 0)
        walkDir(1, 0)

        return result
    }
}
