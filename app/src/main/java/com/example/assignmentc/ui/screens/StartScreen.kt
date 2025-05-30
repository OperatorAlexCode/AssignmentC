package com.example.assignmentc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.assignmentc.R

@Composable
fun StartScreen(
    onStartGame: () -> Unit,
    onShowLeaderboard: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 48.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 100.dp)
                    .fillMaxWidth()
            )

            // Buttons in center
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 100.dp)
            ) {
                Button(
                    onClick = onStartGame,
                    modifier = Modifier.width(200.dp),
                    elevation = ButtonDefaults.buttonElevation(8.dp)
                ) {
                    Text("Start Game")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onShowLeaderboard,
                    modifier = Modifier.width(200.dp),
                    elevation = ButtonDefaults.buttonElevation(8.dp)
                ) {
                    Text("Leaderboard")
                }
            }
        }
    }
}