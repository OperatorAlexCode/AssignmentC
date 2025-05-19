package com.example.assignmentc

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import com.example.assignmentc.ui.theme.AssignmentCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var maze by remember { mutableStateOf(Maze()) }

            AssignmentCTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        MazeDisplay(Modifier.align(Alignment.Center),maze)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun MazeDisplay(modifier: Modifier,toDisplay: Maze) {
    val dimension = 300.dp
    val margin = 4.dp

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
            for (y in 0..toDisplay.Width-1)
            {
                Row(modifier = Modifier.fillMaxSize().weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(margin),
                    verticalAlignment = Alignment.CenterVertically) {
                    for (x in 0..toDisplay.Height-1){
                        var tileColor = Color(0xFFBDBDBD)

                        if (toDisplay.Tiles[x][y].IsWall)
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

                        Box(modifier = Modifier.weight(1f).background(tileColor).fillMaxSize().defaultMinSize(10.dp,10.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AssignmentCTheme {
    }
}