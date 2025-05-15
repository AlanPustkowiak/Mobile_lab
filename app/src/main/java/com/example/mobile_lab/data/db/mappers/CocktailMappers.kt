package com.example.mobile_lab.data.db.mappers

import com.example.mobile_lab.data.db.entities.CocktailEntity
import com.example.mobile_lab.data.db.entities.IngredientEntity
import com.example.mobile_lab.data.db.entities.StepEntity
import com.example.mobile_lab.data.db.relations.CocktailWithIngredientsAndSteps
import com.example.mobile_lab.model.Cocktail
import com.example.mobile_lab.model.Ingredient
import com.example.mobile_lab.model.Step

fun CocktailWithIngredientsAndSteps.toCocktail(): Cocktail {
    val sortedSteps = steps.sortedBy {it.stepOrder}

    return Cocktail(
        id = cocktail.id,
        name = cocktail.name,
        ingredients = ingredients.map {it.toIngredient()},
        steps = sortedSteps.map {it.toStep()},
        imageUrl = cocktail.imageUrl
    )
}

fun IngredientEntity.toIngredient(): Ingredient {
    return Ingredient(
        name = name,
        amount = amount
    )
}

fun StepEntity.toStep(): Step {
    return Step(
        description = description,
        timerDurationSeconds = timerDurationSeconds
    )
}

fun Cocktail.toCocktailEntity(): CocktailEntity{
    return CocktailEntity(
        id = id,
        name = name,
        imageUrl = imageUrl
    )
}

fun Cocktail.toIngredientEntities(): List<IngredientEntity> {
    return ingredients.map{
        IngredientEntity(
            cocktailId = id,
            name = it.name,
            amount = it.amount
        )
    }
}

fun Cocktail.toStepEntities(): List<StepEntity> {
    return steps.mapIndexed{ index, step ->
        StepEntity(
            cocktailId = id,
            description = step.description,
            timerDurationSeconds = step.timerDurationSeconds,
            stepOrder = index
        )
    }
}