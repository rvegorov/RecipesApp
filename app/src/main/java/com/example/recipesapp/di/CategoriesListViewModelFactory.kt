package com.example.recipesapp.di

import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.ui.categories.CategoriesListViewModel

class CategoriesListViewModelFactory(
    private val repository: RecipesRepository,
) : Factory<CategoriesListViewModel> {

    override fun create(): CategoriesListViewModel {
        return CategoriesListViewModel(repository)
    }
}