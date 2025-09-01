package com.example.recipesapp.ui.recipes.recipeList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.API_IMG_URL
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipesListViewModel(val repository: RecipesRepository) : ViewModel() {
    data class RecipesListState(
        var category: Category? = null,
        var categoryImageUrl: String? = null,
        var recipesList: List<Recipe>? = null,
    )

    data class UiMessage(
        var message: String? = null
    )

    private val _state: MutableLiveData<RecipesListState> =
        MutableLiveData<RecipesListState>(RecipesListState())
    val state: LiveData<RecipesListState>
        get() {
            return _state
        }

    private val _uiMessage: MutableLiveData<UiMessage> =
        MutableLiveData<UiMessage>(UiMessage())
    val uiMessage: LiveData<UiMessage>
        get() {
            return _uiMessage
        }


    fun loadRecipesList(category: Category) {
        viewModelScope.launch {
            val recipesListCached = repository.getRecipesByCategoryIdFromCache(category.id)
            if (!recipesListCached.isNullOrEmpty()) {
                _state.postValue(RecipesListState(recipesList = recipesListCached))
            } else {
                _state.postValue(
                    RecipesListState(
                        category = category,
                        categoryImageUrl = "${API_IMG_URL}${category.imageUrl}"
                    )
                )
            }

            val recipesList = repository.getRecipesByCategoryId(category.id)
            val favouritesList = repository.getFavouriteIds()
            if (recipesList == null) {
                _uiMessage.value = UiMessage(message = repository.dataErrorText)
            } else {
                repository.addRecipeList(recipesList, category.id, favouritesList)
            }

            _state.postValue(
                RecipesListState(
                    category = category,
                    recipesList = recipesList,
                    categoryImageUrl = "$API_IMG_URL${category.imageUrl}"
                )
            )

        }
    }
}