package com.example.assignmentc.logic.other

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Animator {
    var CurrentContext: Context
    var SpriteSheet: Bitmap
    var SheetRows: Int = 4
    var SheetColumns: Int = 2
    var CurrentFrame: Int = 0
    var CurrentState: Int = 0

    constructor(context:Context, imageResource:Int) {
        CurrentContext = context
        SpriteSheet = BitmapFactory.decodeResource(context.resources,imageResource)
    }

    constructor(context:Context, imageResource:Int,rows:Int,columns:Int) {
        CurrentContext = context
        SpriteSheet = BitmapFactory.decodeResource(context.resources,imageResource)
        SheetRows = rows
        SheetColumns = columns
    }

    fun Update() {
        CurrentFrame = (CurrentFrame + 1) % SheetColumns
    }

    fun Update(currentState:Int) {
        CurrentState = currentState
        Update()
    }

    fun GetSprite(): Bitmap {
        var height = SpriteSheet.height/SheetRows
        var width = SpriteSheet.width/SheetColumns

        val sprite = Bitmap.createBitmap(
            SpriteSheet,
            width * CurrentFrame,
            height * CurrentState,
            width,
            height
        )

        return sprite
    }
}