package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.API_IMG_URL
import com.example.recipesapp.FAVOURITES_IDS_KEY
import com.example.recipesapp.R
import com.example.recipesapp.SP_NAME
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
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

    private val context = getApplication<Application>()

    init {
        Log.i("!!!", "Recipe View Model object created")
    }

    fun loadRecipe(id: Int) {

        viewModelScope.launch {
            val repository = RecipesRepository(application = application)
            val recipe = repository.getRecipeById(id)
            if (recipe == null) {
                _uiMessage.value = UiMessage(message = context.getString(R.string.dataError))
            }
            _state.value = RecipeState(
                recipe = recipe,
                isFavourite = getFavourites().contains(id.toString()),
                servingsCount = state.value?.servingsCount ?: 1,
                recipeImageUrl = "$API_IMG_URL${recipe?.imageUrl}",
            )
        }
    }

    fun getFavourites(): MutableSet<String> {
        val sharedPrefs = context.getSharedPreferences(
            SP_NAME, MODE_PRIVATE
        )
        val favouritesSet = sharedPrefs?.getStringSet(
            FAVOURITES_IDS_KEY, mutableSetOf()
        ) as MutableSet<String>
        return HashSet<String>(favouritesSet)
    }

    fun onFavoritesClicked() {
        val favouritesSet = getFavourites()
        if (favouritesSet.contains(_state.value?.recipe?.id.toString())) {
            favouritesSet.remove(_state.value?.recipe?.id.toString())
            _state.postValue(_state.value?.copy(isFavourite = false))
        } else {
            favouritesSet.add(_state.value?.recipe?.id.toString())
            _state.postValue(_state.value?.copy(isFavourite = true))
        }
        saveFavourites(favouritesSet)
    }

    fun saveFavourites(ids: Set<String>) {
        val sharedPrefs = context.getSharedPreferences(SP_NAME, MODE_PRIVATE)
        sharedPrefs?.edit {
            putStringSet(FAVOURITES_IDS_KEY, ids)
            apply()
        }
    }

    fun setServings(servingsCount: Int) {
        _state.value = _state.value?.copy(servingsCount = servingsCount)
    }
}