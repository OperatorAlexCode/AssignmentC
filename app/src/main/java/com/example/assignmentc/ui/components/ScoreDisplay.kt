package com.example.assignmentc.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun ScoreDisplay(modifier: Modifier = Modifier,score:Int)
{
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(textAlign = TextAlign.Center,
            fontSize = 20.sp,
            text = "Score:")

        Text(textAlign = TextAlign.Center,
            fontSize = 20.sp,
            text = score.toString())
    }
}