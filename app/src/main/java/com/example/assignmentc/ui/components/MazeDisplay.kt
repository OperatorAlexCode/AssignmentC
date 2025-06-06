package com.example.assignmentc.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.example.assignmentc.R
import com.example.assignmentc.logic.GameManager
import com.example.assignmentc.logic.other.Maze
import androidx.compose.ui.platform.LocalContext
import com.example.assignmentc.logic.items.Effect


@Composable
fun MazeDisplay(modifier: Modifier,toDisplay: Maze) {
    val dimension = 360.dp
    val margin = 0.dp

    Box(modifier = modifier.size(dimension)) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(margin),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            // Upper border
            Row(modifier = Modifier.fillMaxSize().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(margin),
                verticalAlignment = Alignment.CenterVertically) {

                for (x in 0..toDisplay.Size+1){
                    CreateTile(Modifier.weight(1f)
                        .background(Color(0xFF3D3D3D))
                        .fillMaxSize()
                        .defaultMinSize(10.dp,10.dp),
                        R.drawable.wall)
                }
            }

            for (y in 0..<toDisplay.Size)
            {
                Row(modifier = Modifier.fillMaxSize().weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(margin),
                    verticalAlignment = Alignment.CenterVertically) {

                    // Border Left
                    CreateTile(Modifier.weight(1f)
                        .background(Color(0xFF3D3D3D))
                        .fillMaxSize()
                        .defaultMinSize(10.dp,10.dp),
                        R.drawable.wall)

                    for (x in 0..<toDisplay.Size){
                        var image = R.drawable.floor

                        if (toDisplay.Tiles[x][y].IsWall)
                            image = R.drawable.wall

                        CreateTile(Modifier.weight(1f)
                            .background(Color(0xFF3D3D3D))
                            .fillMaxSize()
                            .defaultMinSize(10.dp,10.dp))
                        {
                            Image(bitmap = ImageBitmap.imageResource(image),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                filterQuality = FilterQuality.None)
                        }
                    }

                    // Border right
                    CreateTile(Modifier.weight(1f)
                        .background(Color(0xFF3D3D3D))
                        .fillMaxSize()
                        .defaultMinSize(10.dp,10.dp),
                        R.drawable.wall)
                }
            }

            // Lower border
            Row(modifier = Modifier.fillMaxSize().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(margin),
                verticalAlignment = Alignment.CenterVertically) {

                for (x in 0..toDisplay.Size+1){
                    CreateTile(Modifier.weight(1f)
                        .background(Color(0xFF3D3D3D))
                        .fillMaxSize()
                        .defaultMinSize(10.dp,10.dp),
                        R.drawable.wall)
                }
            }
        }
    }
}

@Composable
fun MazeDisplay(modifier: Modifier,toDisplay: Maze, gameManager: GameManager) {
    val dimension = 360.dp
    val margin = 0.dp

    Box(modifier = modifier.size(dimension)) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(margin),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            // Upper border
            Row(modifier = Modifier.fillMaxSize().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(margin),
                verticalAlignment = Alignment.CenterVertically) {

                for (x in 0..toDisplay.Size+1){
                    CreateTile(Modifier.weight(1f)
                        .background(Color(0xFF3D3D3D))
                        .fillMaxSize()
                        .defaultMinSize(10.dp,10.dp),
                        R.drawable.wall)
                }
            }

            for (y in 0..<toDisplay.Size)
            {
                Row(modifier = Modifier.fillMaxSize().weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(margin),
                    verticalAlignment = Alignment.CenterVertically) {

                    // Border Left
                    CreateTile(Modifier.weight(1f)
                        .background(Color(0xFF3D3D3D))
                        .fillMaxSize()
                        .defaultMinSize(10.dp,10.dp),
                        R.drawable.wall)

                    for (x in 0..<toDisplay.Size){
                        var image = R.drawable.floor

                        if (toDisplay.Tiles[x][y].IsWall)
                            image = R.drawable.wall

                        CreateTile(Modifier.weight(1f)
                            .background(Color(0xFF3D3D3D))
                            .fillMaxSize()
                            .defaultMinSize(10.dp,10.dp))
                        {
                            Image(bitmap = ImageBitmap.imageResource(image),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                filterQuality = FilterQuality.None)

                            // Changed here, imagebitmap to item.getbitmap
                            val itemsHere = gameManager.itemManager.items
                                .filter { it.tile.XPos == x && it.tile.YPos == y }
                            itemsHere.forEach { item ->
                                Image(
                                    // load the banana or peel bitmap from resources
                                    bitmap = item.getBitmap(LocalContext.current),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    filterQuality = FilterQuality.None
                                )
                            }

                            // Draw other sprites here
                            if (gameManager.isPlayerOnTile(x,y))
                            {
                                gameManager.player?.let {
                                    Image(bitmap = it.GetSprite().asImageBitmap(),
                                        contentDescription = "",
                                        modifier = Modifier.fillMaxSize(),
                                        filterQuality = FilterQuality.None)
                                }
                            }

                            else if (gameManager.isEnemyOnTile(x,y))
                            {
                                var enemy = gameManager.getEnemyOnTile(x,y)

                                enemy?.let {
                                    Image(bitmap = it.Animation.GetSprite().asImageBitmap(),
                                        contentDescription = "",
                                        modifier = Modifier.fillMaxSize(),
                                        filterQuality = FilterQuality.None)
                                }
                            }

                            if (gameManager.isEffectOnTile(x,y))
                            {
                                var effect = gameManager.getEffectOnTile(x,y)

                                effect?.let { e ->
                                    e.GetSprite()?.let {
                                        Image(bitmap = it.asImageBitmap(),
                                            contentDescription = "",
                                            modifier = Modifier.fillMaxSize(),
                                            filterQuality = FilterQuality.None)
                                    }
                                }
                            }
                        }
                    }

                    // Border right
                    CreateTile(Modifier.weight(1f)
                        .background(Color(0xFF3D3D3D))
                        .fillMaxSize()
                        .defaultMinSize(10.dp,10.dp),
                        R.drawable.wall)
                }
            }

            // Lower border
            Row(modifier = Modifier.fillMaxSize().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(margin),
                verticalAlignment = Alignment.CenterVertically) {

                for (x in 0..toDisplay.Size+1){
                    CreateTile(Modifier.weight(1f)
                        .background(Color(0xFF3D3D3D))
                        .fillMaxSize()
                        .defaultMinSize(10.dp,10.dp),
                        R.drawable.wall)
                }
            }
        }
    }
}

@Composable
fun CreateTile(modifier:Modifier, image: Int) {
    Box(modifier = modifier) {
        Image(bitmap = ImageBitmap.imageResource(image),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            filterQuality = FilterQuality.None)
    }
}

@Composable
fun CreateTile(modifier:Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier) {
        content()
    }
}