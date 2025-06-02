package com.example.assignmentc.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.assignmentc.R

@Composable
fun HealthBar(
    health: Int,
    maxHealth: Int = 3,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(maxHealth) { index ->
            val heartRes = if (index < health)
                R.drawable.heart_full
            else
                R.drawable.heart_empty
            Image(
                bitmap = ImageBitmap.imageResource(heartRes),
                contentDescription = "Player health",
                modifier = Modifier.size(36.dp),
                filterQuality = FilterQuality.None
            )
        }
    }
}