package com.example.mobile_lab.data

import android.content.Context
import androidx.room.Query
import com.example.mobile_lab.data.db.CocktailDatabase
import com.example.mobile_lab.data.db.mappers.toCocktail
import com.example.mobile_lab.data.db.mappers.toCocktailEntity
import com.example.mobile_lab.data.db.mappers.toIngredientEntities
import com.example.mobile_lab.data.db.mappers.toStepEntities
import com.example.mobile_lab.model.Cocktail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.mobile_lab.R
import com.example.mobile_lab.data.db.entities.CocktailEntity
import com.example.mobile_lab.data.db.entities.IngredientEntity
import com.example.mobile_lab.data.db.entities.StepEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CocktailRepository(private val context: Context) {
    private val cocktailDao = CocktailDatabase.getDatabase(context).cocktailDao()

    init {
        // Przy inicjalizacji repozytorium od razu czyścimy bazę i ładujemy poprawne dane
        CoroutineScope(Dispatchers.IO).launch {
            // Usuwamy wszystkie sampla koktajli, które mają "sample" w ID
            val allCocktails = cocktailDao.getAllCocktails()
            val samplesToDelete = allCocktails.filter { it.id.contains("sample_") }

            if (samplesToDelete.isNotEmpty()) {
                resetDatabase()
            }
        }
    }

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

    suspend fun insertCocktail(cocktail: Cocktail){
        cocktailDao.insertCocktailWithDetails(
            cocktail = cocktail.toCocktailEntity(),
            ingredients = cocktail.toIngredientEntities(),
            steps = cocktail.toStepEntities()
        )
    }

    // Funkcja resetująca bazę danych
    suspend fun resetDatabase() {
        // Usunięcie wszystkich danych
        cocktailDao.deleteAllSteps()
        cocktailDao.deleteAllIngredients()
        cocktailDao.deleteAllCocktails()

        // Ponowne załadowanie danych z pliku JSON
        val inputStream = context.resources.openRawResource(R.raw.sample_cocktails)
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        // Utworzenie klasy na potrzeby parsowania JSON
        data class IngredientJson(val name: String, val amount: String)
        data class StepJson(val description: String, val timerDurationSeconds: Int)
        data class CocktailJson(
            val id: String,
            val name: String,
            val imageUrl: String?,
            val ingredients: List<IngredientJson>,
            val steps: List<StepJson>
        )

        val cocktailsType = object : TypeToken<List<CocktailJson>>() {}.type
        val cocktails: List<CocktailJson> = Gson().fromJson(jsonString, cocktailsType)

        cocktails.forEach { cocktailJson ->
            val cocktailEntity = CocktailEntity(
                id = cocktailJson.id,
                name = cocktailJson.name,
                imageUrl = cocktailJson.imageUrl
            )
            val ingredientEntities = cocktailJson.ingredients.map {
                IngredientEntity(
                    cocktailId = cocktailJson.id,
                    name = it.name,
                    amount = it.amount
                )
            }
            val stepEntities = cocktailJson.steps.mapIndexed { index, step ->
                StepEntity(
                    cocktailId = cocktailJson.id,
                    description = step.description,
                    timerDurationSeconds = step.timerDurationSeconds,
                    stepOrder = index
                )
            }

            cocktailDao.insertCocktailWithDetails(
                cocktail = cocktailEntity,
                ingredients = ingredientEntities,
                steps = stepEntities
            )
        }
    }
}
