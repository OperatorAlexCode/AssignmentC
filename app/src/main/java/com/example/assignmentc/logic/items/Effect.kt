package com.example.assignmentc.logic.items

import android.content.Context
import android.graphics.Bitmap
import com.example.assignmentc.logic.other.Animator
import com.example.assignmentc.logic.other.Tile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class Effect(var context: Context, var tile: Tile, var duration: Float = 1f) {
    abstract var animation: Animator
    var scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    var finished: Boolean = false

    /*init {
        Start()
    }*/

    fun Start() {
        scope.launch(Dispatchers.Main) {
            for (x in 0..animation.SheetColumns) {
                delay((duration/animation.SheetColumns.toFloat()).toLong())
                animation.Update()
            }

            finished = true
        }
    }

    fun GetSprite(): Bitmap? {
        if (!finished)
            return animation.GetSprite()

        return null
    }
}