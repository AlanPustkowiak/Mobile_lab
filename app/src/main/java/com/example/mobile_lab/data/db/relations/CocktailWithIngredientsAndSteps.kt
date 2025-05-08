package com.example.mobile_lab.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.mobile_lab.data.db.entities.CocktailEntity
import com.example.mobile_lab.data.db.entities.IngredientEntity
import com.example.mobile_lab.data.db.entities.StepEntity

data class CocktailWithIngredientsAndSteps(
    @Embedded val cocktail: CocktailEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "cocktailId"
    )
    val ingredients: List<IngredientEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "cocktailId"
    )
    val steps: List<StepEntity>
)
