package com.example.recipesapp.ui.recipes.favourites

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.FAVOURITES_IDS_KEY
import com.example.recipesapp.SP_NAME
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Recipe
import java.util.concurrent.Executors.newFixedThreadPool

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

    private val threadPool = newFixedThreadPool(4)

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
        threadPool.execute {
            val favouritesSet = getFavouritesSet().map { it.toInt() }.toSet()
            val repository = RecipesRepository()
            val recipes: List<Recipe>? = repository.getRecipesByIds(favouritesSet)
            ContextCompat.getMainExecutor(context).execute {
                if (recipes == null) {
                    Toast.makeText(
                        context.applicationContext,
                        "Ошибка получения данных",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            _state.postValue(FavouritesState(recipesList = recipes))
        }
    }
}