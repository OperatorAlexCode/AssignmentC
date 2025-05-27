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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.example.assignmentc.R
import com.example.assignmentc.logic.EnemyManager
import com.example.assignmentc.logic.Maze
import com.example.assignmentc.logic.PlayerManager
import com.example.assignmentc.logic.Tile

@Composable
fun MazeDisplay(modifier: Modifier,toDisplay: Maze, playerManager: PlayerManager, enemyManager: EnemyManager, trapTile: Tile?) {
    val dimension = 360.dp
    val margin = 0.dp

    /*Box(modifier = modifier.size(dimension)) {
        LazyColumn(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(margin),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            items(toDisplay.Tiles) { row ->
                LazyRow(modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(margin),
                    verticalAlignment = Alignment.CenterVertically)
                {
                    items(row)
                    { item ->
                        var tileColor = Color(0xFFBDBDBD)

                        if (item.IsWall)
                            tileColor = Color(0xFF3D3D3D)

                        /*Box(modifier = Modifier.size(20.dp).drawWithCache {
                            val tile = RoundedPolygon(
                                numVertices = 4,
                                radius = size.minDimension/2,
                                centerX = size.width/2,
                                centerY = size.height/2
                            )
                            //Log.d("Shape", "Drawing shape $tile")
                            val path = tile.toPath().asComposePath()
                            onDrawBehind {
                                drawPath(path,color = tileColor)
                            }
                        }.fillMaxSize())*/

                        Box(modifier = Modifier.background(tileColor).fillMaxSize().defaultMinSize(10.dp,10.dp))
                    }
                }
            }
        }
    }*/

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

                            // Draw other sprites here
                            if (playerManager.isOnTile(x,y))
                            {
                                playerManager.player?.let {
                                    Image(bitmap = it.GetSprite().asImageBitmap(),
                                        contentDescription = "",
                                        modifier = Modifier.fillMaxSize(),
                                        filterQuality = FilterQuality.None)
                                }
                            }

                            else if (enemyManager.enemies.any { e -> e.isOnTile(x,y) })
                            {
                                var enemy = enemyManager.enemies.find { e -> e.isOnTile(x,y) }

                                enemy?.let {
                                    Image(bitmap = it.Animation.GetSprite().asImageBitmap(),
                                        contentDescription = "",
                                        modifier = Modifier.fillMaxSize(),
                                        filterQuality = FilterQuality.None)
                                }
                            }

                            // draw trap if present
                            if (trapTile?.XPos == x && trapTile.YPos == y) {   // --added
                                Image(
                                    bitmap = ImageBitmap.imageResource(R.drawable.trap),
                                    contentDescription = "Trap",
                                    modifier = Modifier.fillMaxSize(),
                                    filterQuality = FilterQuality.None
                                )
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