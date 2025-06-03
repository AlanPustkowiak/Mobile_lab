package com.example.mobile_lab.ui.screens

import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.mobile_lab.data.CocktailRepository
import com.example.mobile_lab.model.Cocktail
import com.example.mobile_lab.ui.theme.ThemeViewModel
import androidx.compose.ui.res.painterResource
import com.example.mobile_lab.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailListScreen(
    onCocktailClick: (String) -> Unit,
    themeViewModel: ThemeViewModel
){
    val context = LocalContext.current
    val repository = remember { CocktailRepository(context) }
    val cocktails by repository.getAllCocktails().collectAsState(initial = emptyList())

    val searchQuery = remember { mutableStateOf("") }
    val isSearching = remember { mutableStateOf(false) }

    val filteredCocktails = remember(searchQuery.value, cocktails) {
        if (searchQuery.value.isNotEmpty()) {
            cocktails.filter { it.name.contains(searchQuery.value, ignoreCase = true) }
        } else {
            cocktails
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearching.value){
                      TextField(
                          value = searchQuery.value,
                          onValueChange = { searchQuery.value = it },
                          label = { Text("Wyszukiwanie") },
                          singleLine = true,
                          modifier = Modifier.fillMaxWidth()
                      )
                    }else {
                        Text("Książka barmańska")
                    }
                },
                actions = {
                    // Przycisk zmiany motywu
                    IconButton(onClick = { themeViewModel.toggleTheme() }) {
                        Icon(
                            painter = painterResource(
                                id = if (themeViewModel.isDarkTheme)
                                    R.drawable.light_mode_24dp_e3e3e3_fill0_wght400_grad0_opsz24
                                else
                                    R.drawable.dark_mode_24dp_e3e3e3_fill0_wght400_grad0_opsz24
                            ),
                            contentDescription = if (themeViewModel.isDarkTheme) "Tryb jasny" else "Tryb ciemny",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Przycisk wyszukiwania
                    IconButton(onClick = { isSearching.value = !isSearching.value }) {
                        Icon(
                            imageVector = if (isSearching.value) Icons.Default.Clear else Icons.Default.Search,
                            contentDescription = "Wyszukaj"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        if (cocktails.isEmpty()) {
            // Wyświetl informację, gdy lista jest pusta
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Brak koktajli do wyświetlenia",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            repository.resetDatabase()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Zresetuj bazę danych")
                }
            }
        } else {
            // Wyświetl grid z koktajlami
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(8.dp)
            ) {
                items(filteredCocktails) { cocktail ->
                    CocktailItem(
                        cocktail = cocktail,
                        onClick = { onCocktailClick(cocktail.id) })
                }
            }
        }
    }
}

@Composable
fun CocktailItem(cocktail: Cocktail, onClick: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Obrazek koktajlu z placeholderem
            SubcomposeAsyncImage(
                model = cocktail.imageUrl,
                contentDescription = cocktail.name,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                },
                error = {
                    // Placeholder gdy obrazek nie może zostać załadowany
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxSize(0.5f)
                        )
                    }
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )

            // Nazwa koktajlu
            Text(
                text = cocktail.name,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

