package com.example.recipesapp.ui.recipes.favourites

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.FAVOURITES_IDS_KEY
import com.example.recipesapp.SP_NAME
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Recipe

class FavouritesViewModel(application: Application) : AndroidViewModel(application) {
    data class FavouritesState(
        var recipesList: List<Recipe>? = null
    )

    private val _state: MutableLiveData<FavouritesState> =
        MutableLiveData<FavouritesState>(FavouritesState())
    val state: LiveData<FavouritesState>
        get() {
            return _state
        }

    private fun getFavouritesSet(): Set<String> {
        val sharedPrefs = getApplication<Application>().getSharedPreferences(
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
        val favouritesSet = getFavouritesSet().map { it.toInt() }.toSet()
        _state.value = FavouritesState(
            recipesList = STUB.getRecipesByIds(favouritesSet)
        )
    }
}