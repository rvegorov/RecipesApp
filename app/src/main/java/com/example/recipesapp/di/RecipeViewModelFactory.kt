package com.example.recipesapp.di

import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.ui.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(val repository: RecipesRepository) : Factory<RecipeViewModel> {
    override fun create(): RecipeViewModel {
        return RecipeViewModel(repository)
    }
}