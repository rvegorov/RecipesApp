package com.example.recipesapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.model.Recipe

class RecipeViewModel : ViewModel() {
    data class RecipeState(
        var recipe: Recipe? = null,
        var isFavourite: Boolean = false,
        var servingsCount: Int = 1
    )

    private val _state: MutableLiveData<RecipeState> = MutableLiveData<RecipeState>(RecipeState())
    val state: LiveData<RecipeState>
        get() {
            return _state
        }

    init {
        Log.i("!!!", "View Model object created")
        _state.value = RecipeState(isFavourite = true)
    }
}