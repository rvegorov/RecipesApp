package com.example.recipesapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.API_IMG_URL
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(val repository: RecipesRepository) : ViewModel() {
    data class RecipeState(
        var recipe: Recipe? = null,
        var isFavourite: Boolean = false,
        var servingsCount: Int = 1,
        var recipeImageUrl: String? = null,
        var uiMessage: String? = null
    )

    data class UiMessage(
        var message: String? = null
    )

    private val _state: MutableLiveData<RecipeState> = MutableLiveData<RecipeState>(RecipeState())
    val state: LiveData<RecipeState>
        get() {
            return _state
        }

    private val _uiMessage: MutableLiveData<UiMessage> = MutableLiveData<UiMessage>(UiMessage())
    val uiMessage: LiveData<UiMessage>
        get() {
            return _uiMessage
        }

    init {
        Log.i("!!!", "Recipe View Model object created")
    }

    fun loadRecipe(id: Int) {
        viewModelScope.launch {
            val recipeCached = repository.getRecipeByIdFromCache(id)
            if (recipeCached != null) {
                _state.postValue(RecipeState(recipe = recipeCached))
            }

            val recipe = repository.getRecipeById(id)
            if (recipe == null) {
                _uiMessage.value = UiMessage(message = repository.dataErrorText)
            } else {
                val isFavourite = recipeCached?.isFavourite == true
                val categoryId = recipeCached?.categoryId
                recipe.isFavourite = isFavourite
                recipe.categoryId = categoryId

                _state.value = RecipeState(
                    recipe = recipe,
                    isFavourite = isFavourite,
                    servingsCount = state.value?.servingsCount ?: 1,
                    recipeImageUrl = "$API_IMG_URL${recipe.imageUrl}",
                )
            }
        }
    }

    fun onFavoritesClicked() {
        val recipe = _state.value?.recipe
        val categoryId = recipe?.categoryId
        if (recipe != null && categoryId != null) {
            viewModelScope.launch {
                if (_state.value?.isFavourite == true) {
                    repository.addRecipe(recipe.copy(isFavourite = false), categoryId)
                    _state.postValue(_state.value?.copy(isFavourite = false))
                } else {

                    repository.addRecipe(recipe.copy(isFavourite = true), categoryId)
                    _state.postValue(_state.value?.copy(isFavourite = true))
                }
            }
        }
    }

    fun setServings(servingsCount: Int) {
        _state.value = _state.value?.copy(servingsCount = servingsCount)
    }
}