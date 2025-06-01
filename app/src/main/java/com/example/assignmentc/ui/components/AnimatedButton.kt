package com.example.assignmentc.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.assignmentc.logic.Direction
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnimatedButton(modifier: Modifier, description: String = "button", spriteSheet: Bitmap, button: Int, onClick: () -> Unit, delayTime: Long = 200) {
    val scope = rememberCoroutineScope()

    var buttonSprite: Bitmap by remember {
        mutableStateOf(GetIcon(spriteSheet,button,0))
    }

    IconButton(onClick = {
        buttonSprite = GetIcon(spriteSheet,button,1)
        scope.launch {
            delay(delayTime)
            buttonSprite = GetIcon(spriteSheet,button,0)
        }
        onClick()}, modifier = modifier) {
        //Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Up")
        Image(buttonSprite.asImageBitmap(),
            contentDescription = description,
            filterQuality = FilterQuality.None,
            /*colorFilter = buttonColor*/)
    }
}

fun GetIcon(image: Bitmap, button:Int, isClicked:Int): Bitmap {
    var height: Int = image.height/7
    var width: Int = image.width/2

    return Bitmap.createBitmap(
        image,
        width * isClicked,
        height * button,
        width,
        height
    )
}