package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.FAVOURITES_IDS_KEY
import com.example.recipesapp.SP_NAME
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipeState(
        var recipe: Recipe? = null,
        var isFavourite: Boolean = false,
        var servingsCount: Int = 1,
        var recipeImage: Drawable? = null
    )

    private val _state: MutableLiveData<RecipeState> = MutableLiveData<RecipeState>(RecipeState())
    val state: LiveData<RecipeState>
        get() {
            return _state
        }

    init {
        Log.i("!!!", "View Model object created")
    }

    fun loadRecipe(id: Int?) {
        //TODO("load from somewhere")
        _state.value = RecipeState(
            recipe = STUB.getRecipeById(id),
            isFavourite = getFavourites().contains(id.toString()),
            servingsCount = _state.value?.servingsCount ?: 1
        )

        try {
            val inputStream =
                getApplication<Application>().assets.open(_state.value?.recipe?.imageUrl as String)
            val imageDrawable =
                Drawable.createFromStream(inputStream, _state.value?.recipe?.imageUrl)
            _state.value?.recipeImage = imageDrawable

        } catch (e: Exception) {
            Log.e("assets", e.stackTraceToString())
        }
    }

    fun getFavourites(): MutableSet<String> {
        val sharedPrefs = getApplication<Application>().getSharedPreferences(
            SP_NAME,
            MODE_PRIVATE
        )
        val favouritesSet = sharedPrefs?.getStringSet(
            FAVOURITES_IDS_KEY,
            mutableSetOf()
        ) as MutableSet<String>
        return HashSet<String>(favouritesSet)
    }

    fun onFavoritesClicked() {
        val favouritesSet = getFavourites()
        if (favouritesSet.contains(_state.value?.recipe?.id.toString())) {
            favouritesSet.remove(_state.value?.recipe?.id.toString())
            _state.value = _state.value?.copy(isFavourite = false)
        } else {
            favouritesSet.add(_state.value?.recipe?.id.toString())
            _state.value = _state.value?.copy(isFavourite = true)
        }
        saveFavourites(favouritesSet)
    }

    fun saveFavourites(ids: Set<String>) {
        val sharedPrefs = getApplication<Application>().getSharedPreferences(SP_NAME, MODE_PRIVATE)
        sharedPrefs?.edit {
            putStringSet(FAVOURITES_IDS_KEY, ids)
            apply()
        }
    }
}