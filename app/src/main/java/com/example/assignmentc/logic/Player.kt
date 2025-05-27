package com.example.assignmentc.logic

import android.content.Context
import android.graphics.Bitmap
import com.example.assignmentc.R

class Player(private var context: Context, var currentTile: Tile?) {
    //var item: Item? = null //TODO: implement item "holding"
    var health: Int = 1
    var animation:EntityAnimation = EntityAnimation(context,R.drawable.player)

    fun Update(direction: Direction) {
        animation.Update(direction)
    }

    fun GetSprite(): Bitmap {
        return animation.GetSprite()
    }
}