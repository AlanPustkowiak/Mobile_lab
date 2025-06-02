package com.example.mobile_lab.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.mobile_lab.data.db.entities.CocktailEntity
import com.example.mobile_lab.data.db.entities.IngredientEntity
import com.example.mobile_lab.data.db.entities.StepEntity
import com.example.mobile_lab.data.db.relations.CocktailWithIngredientsAndSteps
import kotlinx.coroutines.flow.Flow

@Dao
interface CocktailDao {
    @Transaction
    @Query("Select * from cocktails")
    fun getAllCocktailsWithDetails(): Flow<List<CocktailWithIngredientsAndSteps>>

    @Query("SELECT * FROM cocktails")
    suspend fun getAllCocktails(): List<CocktailEntity>

    @Transaction
    @Query("Select * from cocktails where id=:cocktailId")
    fun getCocktailWithDetailsById(cocktailId: String): Flow<CocktailWithIngredientsAndSteps?>

    @Query("Select count(*) from cocktails")
    suspend fun getCocktailCount(): Int

    @Query("Delete from cocktails")
    suspend fun deleteAllCocktails()

    @Query("Delete from ingredients")
    suspend fun deleteAllIngredients()

    @Query("Delete from steps")
    suspend fun deleteAllSteps()

    @Query("Select * from cocktails where name like '%' || :query || '%'")
    fun searchCocktauls(query: String): Flow<List<CocktailEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCocktail(cocktail: CocktailEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: List<StepEntity>)

    @Transaction
    suspend fun insertCocktailWithDetails(
        cocktail: CocktailEntity,
        ingredients: List<IngredientEntity>,
        steps: List<StepEntity>
    ){
        insertCocktail(cocktail)
        insertIngredients(ingredients)
        insertSteps(steps)
    }
}
