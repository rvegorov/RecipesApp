package com.example.recipesapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.example.recipesapp.model.Recipe

class RecipeViewModel : ViewModel() {
    data class RecipeState(
        var recipe: Recipe? = null,
        var isFavourite: Boolean = false,
        var servingsCount: Int = 1
    )
}