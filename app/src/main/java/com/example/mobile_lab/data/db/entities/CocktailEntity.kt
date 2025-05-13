package com.example.mobile_lab.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cocktails")
data class CocktailEntity (
    @PrimaryKey val id: String,
    val name: String,
    val imageUrl: String?
)