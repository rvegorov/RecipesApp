package com.example.recipesapp.ui.recipes.recipeList

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipesListState(
        var category: Category? = null,
        var categoryImage: Drawable? = null,
        var recipesList: List<Recipe>? = null,
    )

    private val _state: MutableLiveData<RecipesListState> =
        MutableLiveData<RecipesListState>(RecipesListState())
    val state: LiveData<RecipesListState>
        get() {
            return _state
        }

    fun loadRecipesList(categoryId: Int?) {
        var imageDrawable: Drawable? = null
        try {
            val inputStream =
                getApplication<Application>().assets.open(_state.value?.category?.imageUrl as String)
            imageDrawable =
                Drawable.createFromStream(inputStream, _state.value?.category?.imageUrl)
            _state.value?.categoryImage = imageDrawable

        } catch (e: Exception) {
            Log.e("assets", e.stackTraceToString())
        }

        _state.value = RecipesListState(
            category = STUB.getCategoryById(categoryId),
            recipesList = STUB.getRecipesByCategoryId(categoryId),
            categoryImage = imageDrawable
        )
    }

}