package com.example.mobile_lab.data.db

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mobile_lab.R
import com.example.mobile_lab.data.db.entities.CocktailEntity
import com.example.mobile_lab.data.db.entities.IngredientEntity
import com.example.mobile_lab.data.db.entities.StepEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CocktailDatabaseCallback(
    private val context: Context
) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase){
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            populateDatabase(context)
        }
    }

    // Dodanie obsługi destrukcyjnej migracji
    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
        super.onDestructiveMigration(db)
        CoroutineScope(Dispatchers.IO).launch {
            populateDatabase(context)
        }
    }

    private suspend fun populateDatabase(context: Context){
        try {
            val database = CocktailDatabase.getDatabase(context)
            val cocktailDao = database.cocktailDao()

            // Sprawdzenie czy baza jest już wypełniona
            val existingCount = cocktailDao.getCocktailCount()
            if (existingCount > 0) {
                Log.d("CocktailDatabase", "Baza danych zawiera już $existingCount koktajli, pomijam wypełnianie")
                return
            }

            val inputStream = context.resources.openRawResource(R.raw.sample_cocktails)
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            val cocktailsType = object : TypeToken<List<CocktailJson>>() {}.type
            val cocktails: List<CocktailJson> = Gson().fromJson(jsonString, cocktailsType)

            Log.d("CocktailDatabase", "Znaleziono ${cocktails.size} koktajli do wczytania")

            cocktails.forEach { cocktailJson ->
                val cocktailEntities = CocktailEntity(
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
                    cocktail = cocktailEntities,
                    ingredients = ingredientEntities,
                    steps = stepEntities
                )
            }
            Log.d("CocktailDatabase", "Baza danych została pomyślnie wypełniona")
        } catch (e: Exception){
            Log.e("CocktailDatabase", "Błąd podczas populowania bazy: ${e.message}" )
        }
    }
}

private data class CocktailJson(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val ingredients: List<IngredientJson>,
    val steps: List<StepsJson>
)

private data class IngredientJson(
    val name: String,
    val amount: String
)

private data class StepsJson(
    val description: String,
    val timerDurationSeconds: Int
)
