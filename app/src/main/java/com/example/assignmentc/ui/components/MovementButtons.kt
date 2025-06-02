package com.example.assignmentc.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.assignmentc.R
import com.example.assignmentc.logic.other.Direction

@Composable
fun MovementButtons(
    modifier: Modifier = Modifier,
    onMove: (Direction) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val buttons: Bitmap = BitmapFactory.decodeResource(LocalContext.current.resources,R.drawable.buttons)
    val buttonSize = 50.dp
    val buttonColor = ColorFilter/*.tint(Color.Red, BlendMode.Darken)*/

    /*var buttonN: Bitmap by remember {
        mutableStateOf(GetIcon(buttons,1 + Direction.North.ordinal,0))
    }

    var buttonS: Bitmap by remember {
        mutableStateOf(GetIcon(buttons,1 + Direction.South.ordinal,0))
    }

    var buttonE: Bitmap by remember {
        mutableStateOf(GetIcon(buttons,1 + Direction.East.ordinal,0))
    }

    var buttonW: Bitmap by remember {
        mutableStateOf(GetIcon(buttons,1 + Direction.West.ordinal,0))
    }*/

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Existing movement buttons code
        /*IconButton(onClick = { scope.launch {
                delay(200)
                buttonN = GetIcon(buttons,1 + Direction.North.ordinal,0)
            }
            buttonN = GetIcon(buttons,1 + Direction.North.ordinal,1)
            onMove(Direction.North) }, modifier = Modifier.size(buttonSize).background(Color.Transparent)) {
            //Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Up")
            Image(buttonN.asImageBitmap(),
                contentDescription = "Up",
                filterQuality = FilterQuality.None,
                /*colorFilter = buttonColor*/)
        }*/

        AnimatedButton(
            modifier = Modifier.size(buttonSize).background(Color.Transparent),
            description = "Up",
            spriteSheet = buttons,
            button = 1 + Direction.North.ordinal,
            onClick = {onMove(Direction.North)})

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /*IconButton(onClick = { scope.launch {
                    delay(200)
                    buttonW = GetIcon(buttons,1 + Direction.West.ordinal,0)
                }
                buttonW = GetIcon(buttons,1 + Direction.West.ordinal,1)
                onMove(Direction.West) }, modifier = Modifier.size(buttonSize).background(Color.Transparent)) {
                //Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = "Left")
                Image(buttonW.asImageBitmap(),
                    contentDescription = "Left",
                    filterQuality = FilterQuality.None,
                    /*colorFilter = buttonColor*/)
            }*/

            AnimatedButton(
                modifier = Modifier.size(buttonSize).background(Color.Transparent),
                description = "Left",
                spriteSheet = buttons,
                button = 1 + Direction.West.ordinal,
                onClick = {onMove(Direction.West)})

            Spacer(modifier = Modifier.size(buttonSize))

            /*IconButton(onClick = { scope.launch {
                    delay(200)
                    buttonE = GetIcon(buttons,1 + Direction.East.ordinal,0)
                }
                buttonE = GetIcon(buttons,1 + Direction.East.ordinal,1)
                onMove(Direction.East) }, modifier = Modifier.size(buttonSize).background(Color.Transparent)) {
                //Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Right")
                Image(buttonE.asImageBitmap(),
                    contentDescription = "Right",
                    filterQuality = FilterQuality.None,
                    /*colorFilter = buttonColor*/)
            }*/

            AnimatedButton(
                modifier = Modifier.size(buttonSize).background(Color.Transparent),
                description = "Right",
                spriteSheet = buttons,
                button = 1 + Direction.East.ordinal,
                onClick = {onMove(Direction.East)})
        }

        /*IconButton(onClick = { scope.launch {
                delay(200)
                buttonS = GetIcon(buttons,1 + Direction.South.ordinal,0)
            }
            buttonS = GetIcon(buttons,1 + Direction.South.ordinal,1)
            onMove(Direction.South) }, modifier = Modifier.size(buttonSize).background(Color.Transparent)) {
            //Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "South")
            Image(buttonS.asImageBitmap(),
                contentDescription = "South",
                filterQuality = FilterQuality.None,
                /*colorFilter = buttonColor*/)
        }*/

        AnimatedButton(
            modifier = Modifier.size(buttonSize).background(Color.Transparent),
            description = "Down",
            spriteSheet = buttons,
            button = 1 + Direction.South.ordinal,
            onClick = {onMove(Direction.South)})
    }
}