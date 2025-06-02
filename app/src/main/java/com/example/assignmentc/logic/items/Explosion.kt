package com.example.assignmentc.logic.items

import android.content.Context
import com.example.assignmentc.logic.other.Animator
import com.example.assignmentc.logic.other.Tile
import com.example.assignmentc.R

class Explosion(context: Context, tile: Tile, duration: Float = 1f): Effect(context,tile,duration)
{
    override var animation: Animator = Animator(context,R.drawable.explosion,1,3)
}