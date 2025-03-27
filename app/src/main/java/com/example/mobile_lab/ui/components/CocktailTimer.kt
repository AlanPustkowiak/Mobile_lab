package com.example.mobile_lab.ui.components

import android.os.CountDownTimer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CocktailTimer(durationSeconds: Int){
    var timeLeft by remember { mutableStateOf(durationSeconds) }
    var isRunning by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }

    val timer = remember {
        object : CountDownTimer(timeLeft * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = (millisUntilFinished / 1000).toInt()
            }
            override fun onFinish() {
                timeLeft = 0
                isRunning = false
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            timer.cancel()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Czas ${timeLeft/60}:${(timeLeft%60).toString().padStart(2,'0')}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = {
                if (!isRunning && !isPaused){
                    timer.start()
                    isRunning = true
                } else if (isPaused){
                    timer.cancel()
                    object : CountDownTimer(timeLeft * 1000L, 1000){
                        override fun onTick(millisUnitField: Long){
                            timeLeft = (millisUnitField/1000).toInt()
                        }

                        override fun onFinish(){
                            timeLeft = 0
                            isRunning = false
                            isPaused = false
                        }
                    }.start()
                    isRunning = true
                    isPaused = false
                }
            },
            enabled = !isPaused || isPaused
        ) {
            Text(if (isPaused) "Wznów" else "Start")
        }

        Button(
            onClick = {
                if (isRunning && !isPaused){
                    timer.cancel()
                    isRunning = false
                    isPaused = true
                }
            },
            enabled = isRunning && !isPaused
        ) {
            Text("Pauza")
        }

        Button(
            onClick = {
                timer.cancel()
                timeLeft = durationSeconds
                isRunning = false
                isPaused = false
            },
            enabled = isRunning || isPaused
        ) {
            Text("Reset")
        }
    }

}