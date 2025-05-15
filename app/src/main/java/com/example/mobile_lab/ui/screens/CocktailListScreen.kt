package com.example.mobile_lab.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mobile_lab.data.CocktailRepository
import com.example.mobile_lab.model.Cocktail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailListScreen(onCocktailClick: (String) -> Unit){
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
                    IconButton(onClick = { isSearching.value = !isSearching.value }) {
                        Icon(
                            imageVector = Icons.Default.Search,
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredCocktails) { cocktail ->
                CocktailItem(
                    cocktail = cocktail,
                    onClick = { onCocktailClick(cocktail.id) })
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = cocktail.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
        }
    }
}