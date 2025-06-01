package com.example.assignmentc.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.example.assignmentc.R
import com.example.assignmentc.logic.GameManager

@Composable
fun ItemBox(modifier: Modifier = Modifier, gameManager: GameManager)
{
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(modifier = Modifier.size(80.dp).background(Color.Black)) {
            // Placeholder for testing
            Image(bitmap = ImageBitmap.imageResource(R.drawable.wall),
                contentDescription = "",
                modifier = Modifier.padding(10.dp).fillMaxSize(),
                filterQuality = FilterQuality.None)
        }

        Spacer(modifier = Modifier.size(10.dp))

        AnimatedButton(modifier = Modifier.size(50.dp).background(Color.Transparent),
            description = "Use Item",
            spriteSheet = BitmapFactory.decodeResource(LocalContext.current.resources,R.drawable.buttons),
            button = 0,
            onClick = { /*Call Relevant game manager for using item*/ })
    }
}