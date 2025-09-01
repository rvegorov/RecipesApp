package com.example.recipesapp.ui.recipes.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class FavouritesViewModel(val repository: RecipesRepository) : ViewModel() {
    data class FavouritesState(
        var recipesList: List<Recipe>? = null
    )

    data class UiMessage(
        var message: String? = null
    )

    private val _state: MutableLiveData<FavouritesState> =
        MutableLiveData<FavouritesState>(FavouritesState())
    val state: LiveData<FavouritesState>
        get() {
            return _state
        }

    private val _uiMessage: MutableLiveData<UiMessage> = MutableLiveData<UiMessage>(UiMessage())
    val uiMessage: LiveData<UiMessage>
        get() {
            return _uiMessage
        }

    fun loadRecipesList() {
        viewModelScope.launch {
            val recipes: List<Recipe>? = repository.getFavouriteRecipes()
            if (recipes == null) {
                _uiMessage.value = UiMessage(message = repository.dataErrorText)
            }
            _state.postValue(FavouritesState(recipesList = recipes))
        }
    }
}