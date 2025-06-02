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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.assignmentc.R
import com.example.assignmentc.logic.items.Item

@Composable
fun ItemBox(modifier: Modifier = Modifier, item: Item?, onClick: () -> Unit)
{
    //var displayItem by remember { mutableStateOf<Item?>(item) }
    var itemUsed by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(modifier = Modifier.size(80.dp).background(Color.Black)) {

            if (item != null && !itemUsed)
            {
                item?.let {
                    Image(bitmap = it.getBitmap(LocalContext.current),
                        contentDescription = "",
                        modifier = Modifier.padding(10.dp).fillMaxSize(),
                        filterQuality = FilterQuality.None)
                }
            }
        }

        Spacer(modifier = Modifier.size(10.dp))

        AnimatedButton(modifier = Modifier.size(50.dp).background(Color.Transparent),
            description = "Use Item",
            spriteSheet = BitmapFactory.decodeResource(LocalContext.current.resources,R.drawable.buttons),
            button = 0,
            onClick = {
                onClick()
                itemUsed = true
            })
    }
}