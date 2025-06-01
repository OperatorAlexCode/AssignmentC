package com.example.assignmentc.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.assignmentc.logic.GameManager

@Composable
fun ScoreDisplay(modifier: Modifier = Modifier,gameManager: GameManager)
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
            text = gameManager.score.toString())
    }
}