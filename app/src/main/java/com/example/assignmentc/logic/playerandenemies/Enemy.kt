package com.example.assignmentc.logic.playerandenemies

import android.content.Context
import android.graphics.Bitmap
import com.example.assignmentc.R
import com.example.assignmentc.logic.other.Animator
import com.example.assignmentc.logic.other.Tile

class Enemy(var context: Context, var currentTile: Tile) {
    var Animation: Animator = Animator(context, R.drawable.enemy)
    var freezeTurns: Int = 0

    fun move(playerTile: Tile, isTileOccupied: (Tile) -> Boolean, onHitPlayer: () -> Unit) {
        if (freezeTurns > 0) {
            freezeTurns--
            return
        }

        val path = EnemyManager.findPath(currentTile, playerTile) ?: return
        if (path.size <= 1) return

        val nextTile = path[1]

        // Skip if another enemy is already moving there
        if (isTileOccupied(nextTile)) return

        val direction = currentTile.directionTo(nextTile)
        if (direction != null) {
            currentTile = nextTile
            Animation.Update(direction.ordinal)

            if (currentTile == playerTile) {
                onHitPlayer()
                freezeTurns = 2
            }
        }
    }


    fun getLocationTile(): Tile {
        return currentTile
    }

    fun getSprite(): Bitmap {
        return Animation.GetSprite()
    }

    fun isOnTile(x:Int,y:Int): Boolean {
        return currentTile.XPos == x && currentTile.YPos == y
    }
}