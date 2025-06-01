package com.example.assignmentc.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignmentc.logic.BlockMaze
import com.example.assignmentc.logic.Direction
import com.example.assignmentc.logic.LineMaze
import com.example.assignmentc.logic.Maze
import com.example.assignmentc.logic.TempleMaze
import com.example.assignmentc.ui.viewmodels.MazePickerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.assignmentc.R
import com.example.assignmentc.logic.GameManager
import com.example.assignmentc.logic.Player
import com.example.assignmentc.ui.components.AnimatedButton
import com.example.assignmentc.ui.components.MazeDisplay

@Composable
fun MazePickerScreen(
    onMazeSelected: (Maze) -> Unit,
    onCancel: () -> Unit,
    viewModel: MazePickerViewModel = viewModel()
) {
    val context = LocalContext.current
    val mazeTypes = remember {
        listOf(
            Maze() to "Default maze",
            BlockMaze() to "Block Maze",
            LineMaze() to "Line Maze",
            TempleMaze() to "Temple Maze"
        )
    }
    val currentMazeIndex = viewModel.currentMazeIndex
    val currentMaze = mazeTypes[currentMazeIndex].first

    // Create a dummy GameManager for preview purposes
    val dummyGameManager = remember(currentMaze) {
        GameManager(context, currentMaze).apply {
            player = Player(context, currentMaze.Tiles[currentMaze.Size / 2][currentMaze.Size / 2])
        }
    }

    val buttonSpriteSheet = BitmapFactory.decodeResource(LocalContext.current.resources,R.drawable.buttons)

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Select Maze",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Maze preview display
            MazePreviewDisplay(
                modifier = Modifier
                    .size(300.dp)
                    .background(Color(0xFF333333)),
                maze = currentMaze
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                AnimatedButton(modifier = Modifier.size(50.dp),
                    description = "Left",
                    spriteSheet = buttonSpriteSheet,
                    button = 1 + Direction.West.ordinal,
                    onClick = { viewModel.prevMaze(mazeTypes.size) })

                Text(text = "${currentMazeIndex + 1}/${mazeTypes.size}")

                AnimatedButton(modifier = Modifier.size(50.dp),
                    description = "Right",
                    spriteSheet = buttonSpriteSheet,
                    button = 1 + Direction.East.ordinal,
                    onClick = { viewModel.nextMaze(mazeTypes.size) })
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                /*Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel")
                }*/

                AnimatedButton(modifier = Modifier.size(50.dp),
                    description = "Cancel",
                    spriteSheet = buttonSpriteSheet,
                    button = 6,
                    onClick = onCancel)

                /*Button(
                    onClick = { onMazeSelected(currentMaze) }
                ) {
                    Text("Start Game")
                }*/

                AnimatedButton(modifier = Modifier.size(50.dp),
                    description = "Select",
                    spriteSheet = buttonSpriteSheet,
                    button = 5,
                    onClick = { onMazeSelected(currentMaze) })
            }
        }
    }
}

@Composable
fun MazePreviewDisplay(
    modifier: Modifier,
    maze: Maze
) {
    Box(modifier = modifier) {
        MazeDisplay(
            modifier = Modifier.fillMaxSize(),
            toDisplay = maze
        )
    }
}