package com.example.mobile_lab.data

import android.content.Context
import androidx.room.Query
import com.example.mobile_lab.data.db.CocktailDatabase
import com.example.mobile_lab.data.db.mappers.toCocktail
import com.example.mobile_lab.model.Cocktail
import com.example.mobile_lab.model.Ingredient
import com.example.mobile_lab.model.Step
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CocktailRepository(private val context: Context) {
    private val cocktailDao = CocktailDatabase.getDatabase(context).cocktailDao()

    fun getAllCocktails(): Flow<List<Cocktail>> {
        return cocktailDao.getAllCocktailsWithDetails().map { list ->
            list.map {it.toCocktail()}
        }
    }

    fun getCocktailById(id: String): Flow<Cocktail?> {
        return cocktailDao.getCocktailWithDetailsById(id).map { it?.toCocktail() }
    }

    fun searchCocktails(query: String): Flow<List<Cocktail>> {
        return cocktailDao.getAllCocktailsWithDetails().map {
            list -> list.map { it.toCocktail() }
            .filter { it.name.contains(query, ignoreCase = true) }
        }
    }

}