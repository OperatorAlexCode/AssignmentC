package com.example.assignmentc.logic

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.animation.core.Animation
import com.example.assignmentc.R

class Enemy(var context: Context, var currentTile: Tile) {
    var Animation: EntityAnimation = EntityAnimation(context,R.drawable.enemy)

    fun move() {
        val directions = Direction.entries.shuffled()

        for (direction in directions) {
            val nextTile = currentTile.GetTile(direction)
            if (nextTile != null) {
                currentTile = nextTile
                Animation.Update(direction)
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