package com.example.assignmentc.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 48.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            )

            PlayerSprite(Modifier.size(200.dp))

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onStartGame,
                modifier = Modifier.width(200.dp),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text("Play")
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

@Composable
fun PlayerSprite(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val bitmap = remember {
        val spriteSheet = BitmapFactory.decodeResource(context.resources, R.drawable.player)
        val rows = 4
        val columns = 2
        val frameWidth = spriteSheet.width / columns
        val frameHeight = spriteSheet.height / rows

        Bitmap.createBitmap(
            spriteSheet,
            frameWidth * 1,
            frameHeight * 2,
            frameWidth,
            frameHeight
        )
    }

    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "Player Character",
        modifier = modifier,
        contentScale = ContentScale.Fit,
        filterQuality = FilterQuality.None
    )
}