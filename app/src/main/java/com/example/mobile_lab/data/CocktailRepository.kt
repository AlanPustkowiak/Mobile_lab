package com.example.mobile_lab.data

import com.example.mobile_lab.model.Cocktail
import com.example.mobile_lab.model.Ingredient
import com.example.mobile_lab.model.Step

class CocktailRepository {
    fun getCocktails(): List<Cocktail>{
        return listOf(
            Cocktail(
                id = "1",
                name = "Mojito",
                ingredients = listOf(
                    Ingredient("Biały rum", "50 ml"),
                    Ingredient("Limonka", "1 sztuka"),
                    Ingredient("Cukier trzcinowy", "2 łyżeczki"),
                    Ingredient("Mięta", "kilka listków"),
                    Ingredient("Woda gazowana", "do uzupełnienia"),
                    Ingredient("Kruszony lód", "do szklanki")
                ),
                steps = listOf(
                    Step("Limonkę pokrój na ćwiartki i wrzuć do szklanki"),
                    Step("Dodaj cukier i utłucz muddlerem"),
                    Step("Dodaj listki mięty i delikatnie ugnieć"),
                    Step("Napełnij szklankę kruszonym lodem"),
                    Step("Wlej rum i dokładnie wymieszaj przez 30 sekund", 30),
                    Step("Dopełnij wodą gazowaną i delikatnie wymieszaj"),
                    Step("Udekoruj listkiem mięty")
                )
            ),
            Cocktail(
                id = "2",
                name = "Margarita",
                ingredients = listOf(
                    Ingredient("Tequila", "60 ml"),
                    Ingredient("Triple sec", "30 ml"),
                    Ingredient("Sok z limonki", "30 ml"),
                    Ingredient("Sól", "do dekoracji"),
                    Ingredient("Lód", "do shakera")
                ),
                steps = listOf(
                    Step("Krawędź kieliszka przetrzyj cząstką limonki i zanurz w soli"),
                    Step("Wrzuć lód do shakera"),
                    Step("Dodaj tequilę, triple sec i sok z limonki"),
                    Step("Energicznie wstrząsaj przez 15 sekund", 15),
                    Step("Przecedź do przygotowanego kieliszka")
                )
            )
        )
    }

    fun getCocktailById(id: String): Cocktail? {
        return getCocktails().find { it.id == id }
    }
}