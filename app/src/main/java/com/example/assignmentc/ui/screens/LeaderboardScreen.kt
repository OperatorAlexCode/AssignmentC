package com.example.assignmentc.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.assignmentc.ui.components.ScoreEntry

data class ScoreEntryData(
    val name: String,
    val score: Int
)

@Composable
fun LeaderboardScreen(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf("local") }

    // Sample data
    val localScores = remember {
        listOf(
            ScoreEntryData("Player1", 2450),
            ScoreEntryData("Player2", 1800),
            ScoreEntryData("BestPlayer", 3000),
            ScoreEntryData("Newbie", 750),
            ScoreEntryData("TestUser", 1500)
        ).sortedByDescending { it.score }
    }

    val onlineScores = remember {
        listOf(
            ScoreEntryData("GlobalChamp", 9820),
            ScoreEntryData("ProPlayer", 7650),
            ScoreEntryData("SkyMaster", 6800),
            ScoreEntryData("DragonSlayer", 5500),
            ScoreEntryData("Beginner", 3200)
        ).sortedByDescending { it.score }
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Tab selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TabButton(
                text = "Local",
                isSelected = selectedTab == "local",
                onClick = { selectedTab = "local" }
            )
            TabButton(
                text = "Online",
                isSelected = selectedTab == "online",
                onClick = { selectedTab = "online" }
            )
        }

        // Leaderboard list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(if (selectedTab == "local") localScores else onlineScores,
                key = { it.name }) { entry ->
                ScoreEntry(entry = entry)
            }
        }
    }
}

@Composable
private fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        modifier = modifier.width(120.dp)
    ) {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardScreenPreview() {
    LeaderboardScreen()
}