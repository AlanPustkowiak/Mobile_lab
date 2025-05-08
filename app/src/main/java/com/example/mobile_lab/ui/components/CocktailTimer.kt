package com.example.mobile_lab.ui.components

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_lab.ui.viewmodels.CocktailTimerViewModel

//TODO - zmiana na viewmodel
@Composable
fun CocktailTimer(durationSeconds: Int, stepId: String, viewModel: CocktailTimerViewModel = viewModel(key = stepId)){
    LaunchedEffect(key1 = Unit) {
        viewModel.initialize(durationSeconds)
    }

    val timeLeft by viewModel.timeLeft.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val isPaused by viewModel.isPaused.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ){
        Text(
            text = "Czas ${timeLeft/60}:${(timeLeft%60).toString().padStart(2,'0')}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Button(
            onClick = {viewModel.startTimer()},
            enabled = !isRunning || isPaused
        ) {
            Text(if(isPaused) "Wznów" else "Start")
        }

        Button(
            onClick = {viewModel.pauseTimer()},
            enabled =  isRunning || !isPaused
        ) {
            Text("Pauza")
        }

        Button(
            onClick = {viewModel.resetTimer()},
            enabled = isRunning || isPaused
        ) {
            Text("Reset")
        }
    }

}