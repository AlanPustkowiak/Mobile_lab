package com.example.mobile_lab.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobile_lab.data.CocktailRepository
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale
import android.content.Intent
import android.net.Uri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mobile_lab.model.Cocktail
import com.example.mobile_lab.ui.components.CocktailTimer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailDetailsScreen(cocktailId: String, onNavigateBack: () -> Unit){
    val context = LocalContext.current
    val repository =  remember { CocktailRepository(context) }
    val cocktail by repository.getCocktailById(cocktailId).collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(cocktail?.name ?: "Szczegóły koktajlu") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Powrót"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            if (cocktail != null) {
                FloatingActionButton(
                    onClick = {
                        // Przygotowanie treści SMS ze składnikami
                        val smsBody = buildString {
                            append("Składniki dla koktajlu ${cocktail!!.name}:\n\n")
                            cocktail!!.ingredients.forEach { ingredient ->
                                append("- ${ingredient.name}: ${ingredient.amount}\n")
                            }
                        }

                        // Utworzenie i uruchomienie intencji SMS
                        val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("smsto:")
                            putExtra("sms_body", smsBody)
                        }
                        context.startActivity(smsIntent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Wyślij składniki przez SMS"
                    )
                }
            }
        }
    ) { paddingValues ->
        if (cocktail != null) {
            CocktailDetails(
                cocktail = cocktail!!,
                modifier = Modifier.padding(paddingValues)
            )
        }
        else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ){
                Text("Nie znaleziono koktajlu")
            }
        }
    }
}

@Composable
fun CocktailDetails(cocktail: Cocktail, modifier: Modifier = Modifier){
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Duży obrazek koktajlu
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(cocktail.imageUrl ?: "https://via.placeholder.com/400x300?text=Cocktail")
                .crossfade(true)
                .build(),
            contentDescription = cocktail.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        )

        Text(
            text = "Składniki:",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        cocktail.ingredients.forEach { ingredient ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = ingredient.name)
                Text(text = ingredient.amount)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sposób przygotowania:",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        cocktail.steps.forEachIndexed { index, step ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ){
                Text(
                    text = "${index+1}. ${step.description}",
                    style = MaterialTheme.typography.bodyLarge
                )

                if (step.timerDurationSeconds > 0){
                    Spacer(modifier = Modifier.height(8.dp))
                    CocktailTimer(durationSeconds = step.timerDurationSeconds,
                        stepId = "${cocktail.id}_step_$index")
                }
            }
        }
    }
}


