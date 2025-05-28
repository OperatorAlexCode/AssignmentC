package com.example.assignmentc.logic

import android.content.Context
import android.graphics.Bitmap
import com.example.assignmentc.R

class Enemy(var context: Context, var currentTile: Tile) {
    var Animation: Animator = Animator(context,R.drawable.enemy)

    fun move(playerTile: Tile) {
        val path = EnemyManager.findPath(currentTile, playerTile)
        if (path != null && path.size > 1) {
            val nextTile = path[1] // 0 = current position, 1 = next step
            val direction = currentTile.directionTo(nextTile)
            if (direction != null) {
                currentTile = nextTile
                Animation.Update(direction.ordinal)
                break
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