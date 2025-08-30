package com.example.recipesapp.di

import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.ui.recipes.favourites.FavouritesViewModel

class FavouritesViewModelFactory(val repository: RecipesRepository) : Factory<FavouritesViewModel> {
    override fun create(): FavouritesViewModel {
        return FavouritesViewModel(repository)
    }
}