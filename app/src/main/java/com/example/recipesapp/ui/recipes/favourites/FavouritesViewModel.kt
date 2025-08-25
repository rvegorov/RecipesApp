package com.example.recipesapp.ui.recipes.favourites

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.FAVOURITES_IDS_KEY
import com.example.recipesapp.R
import com.example.recipesapp.SP_NAME
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class FavouritesViewModel(application: Application) : AndroidViewModel(application) {
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

    private val context = getApplication<Application>()

    private fun getFavouritesSet(): Set<String> {
        val sharedPrefs = context.getSharedPreferences(
            SP_NAME,
            MODE_PRIVATE
        )
        val favouritesSet = sharedPrefs?.getStringSet(
            FAVOURITES_IDS_KEY,
            setOf()
        ) as Set<String>
        return HashSet<String>(favouritesSet)
    }

    fun loadRecipesList() {
        viewModelScope.launch {
            val favouritesSet = getFavouritesSet().map { it.toInt() }.toSet()
            val recipes: List<Recipe>? = RecipesRepository.getRecipesByIds(favouritesSet)
            if (recipes == null) {
                _uiMessage.value = UiMessage(message = context.getString(R.string.dataError))
            }
            _state.postValue(FavouritesState(recipesList = recipes))
        }
    }
}