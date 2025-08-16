package com.example.recipesapp.ui.recipes.recipeList

import android.app.Application
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.API_IMG_URL
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import java.util.concurrent.Executors.newFixedThreadPool

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipesListState(
        var category: Category? = null,
        var categoryImageUrl: String? = null,
        var recipesList: List<Recipe>? = null,
    )

    private val _state: MutableLiveData<RecipesListState> =
        MutableLiveData<RecipesListState>(RecipesListState())
    val state: LiveData<RecipesListState>
        get() {
            return _state
        }
    private val context = getApplication<Application>()
    private val threadPool = newFixedThreadPool(4)

    fun loadRecipesList(category: Category) {
        threadPool.execute {
            val repository = RecipesRepository()
            val recipesList = repository.getRecipesByCategoryId(category.id)

            ContextCompat.getMainExecutor(context).execute {
                if (recipesList == null) {
                    Toast.makeText(
                        context, "Ошибка получения данных", Toast.LENGTH_LONG
                    ).show()
                }
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