package com.example.recipesapp.di

import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.ui.recipes.recipeList.RecipesListViewModel

class RecipesListViewModelFactory(val repository: RecipesRepository) :
    Factory<RecipesListViewModel> {
    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(repository)
    }
}