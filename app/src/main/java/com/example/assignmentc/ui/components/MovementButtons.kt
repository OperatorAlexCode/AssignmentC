package com.example.assignmentc.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.assignmentc.logic.Direction

@Composable
fun MovementButtons(
    modifier: Modifier = Modifier,
    onMove: (Direction) -> Unit,
    onShowLeaderboard: () -> Unit
) {
    val buttonSize = 50.dp
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Existing movement buttons code
        IconButton(onClick = { onMove(Direction.North) }, modifier = Modifier.size(buttonSize)) {
            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Up")
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onMove(Direction.West) }, modifier = Modifier.size(buttonSize)) {
                Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = "Left")
            }

            Spacer(modifier = Modifier.size(buttonSize))

            IconButton(onClick = { onMove(Direction.East) }, modifier = Modifier.size(buttonSize)) {
                Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Right")
            }
        }

        IconButton(onClick = { onMove(Direction.South) }, modifier = Modifier.size(buttonSize)) {
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "South")
        }

        Button(
            onClick = onShowLeaderboard,
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text("Show Leaderboard")
        }
    }
}