package com.example.mobile_lab.model

data class Ingredient(
    val name: String,
    val amount: String
)

data class Step(
    val description: String,
    val timerDurationSeconds: Int = 0
)

data class Cocktail(
    val id: String,
    val name: String,
    val ingredients: List<Ingredient>,
    val steps: List<Step>,
    val imageUrl: String? = null
)