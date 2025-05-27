package com.example.assignmentc.logic

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class EntityAnimation {
    var CurrentContext: Context
    var SpriteSheet: Bitmap
    var SheetRows: Int = 4
    var SheetColumns: Int = 2
    var AnimationState: Int = 0
    var MoveDirection: Direction = Direction.North

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
        AnimationState = (AnimationState + 1) % SheetColumns
    }

    fun Update(newDirection: Direction) {
        MoveDirection = newDirection
        Update()
    }

    fun GetSprite(): Bitmap {
        var height = SpriteSheet.height/SheetRows
        var width = SpriteSheet.width/SheetColumns

        val sprite = Bitmap.createBitmap(
            SpriteSheet,
            width * AnimationState,
            height * MoveDirection.ordinal,
            width,
            height
        )

        return sprite
    }
}