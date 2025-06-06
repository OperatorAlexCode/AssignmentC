package com.example.assignmentc.logic.playerandenemies

import android.content.Context
import android.graphics.Bitmap
import com.example.assignmentc.R
import com.example.assignmentc.logic.other.Animator
import com.example.assignmentc.logic.other.Direction
import com.example.assignmentc.logic.other.Tile

class Player(private var context: Context, var currentTile: Tile?) {
    //var item: Item? = null //TODO: implement item "holding"
    var health: Int = 0
    var maxHealth: Int = 3
    var animation: Animator = Animator(context, R.drawable.player)

    fun Update(direction: Direction) {
        animation.Update(direction.ordinal)
    }

    fun GetSprite(): Bitmap {
        return animation.GetSprite()
    }

    fun IsOnTile(x:Int,y:Int): Boolean {
        return currentTile?.XPos == x && currentTile?.YPos == y
    }

    fun Hurt(dmg:Int = 1) {
        health -= dmg
        health = health.coerceIn(0,maxHealth)
    }

    fun Heal(hp:Int = 1) {
        health += hp
        health = health.coerceIn(0,maxHealth)
    }

    fun HealMax() {
        health = maxHealth
    }
}