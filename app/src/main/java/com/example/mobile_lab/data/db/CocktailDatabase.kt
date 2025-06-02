package com.example.mobile_lab.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mobile_lab.data.db.dao.CocktailDao
import com.example.mobile_lab.data.db.entities.CocktailEntity
import com.example.mobile_lab.data.db.entities.IngredientEntity
import com.example.mobile_lab.data.db.entities.StepEntity

@Database(
    entities = [
        CocktailEntity::class,
        IngredientEntity::class,
        StepEntity::class
    ],
    version = 2
)
abstract class CocktailDatabase : RoomDatabase() {
    abstract fun cocktailDao(): CocktailDao

    companion object{
        @Volatile
        private var INSTANCE: CocktailDatabase? = null

        fun getDatabase(context: Context): CocktailDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CocktailDatabase::class.java,
                    "cocktail_database"
                )
                    .fallbackToDestructiveMigration() // Dodanie destrukcyjnej migracji
                    .addCallback(CocktailDatabaseCallback(context.applicationContext))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

