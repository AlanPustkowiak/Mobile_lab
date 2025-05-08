package com.example.mobile_lab.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ingredients",
    foreignKeys = [
        ForeignKey(
            entity = CocktailEntity::class,
            parentColumns = ["id"],
            childColumns = ["cocktailId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class IngredientEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cocktailId: String,
    val name: String,
    val amount: String
)