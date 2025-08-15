package com.example.recipesapp.ui.categories

import android.app.Application
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Category
import java.util.concurrent.Executors.newFixedThreadPool

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {
    data class CategoriesListState(
        var categoriesList: List<Category>? = null
    )

    private val threadPool = newFixedThreadPool(4)

    val context = getApplication<Application>()

    private val _state: MutableLiveData<CategoriesListState> =
        MutableLiveData<CategoriesListState>(CategoriesListState())
    val state: LiveData<CategoriesListState>
        get() {
            return _state
        }

    fun loadCategoriesList() {
        threadPool.execute {
            val repository = RecipesRepository()
            val categoriesList = repository.getCategories()
            ContextCompat.getMainExecutor(context).execute {
                if (categoriesList == null) {
                    Toast.makeText(
                        context.applicationContext,
                        "Ошибка получения данных",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            _state.postValue(CategoriesListState(categoriesList = categoriesList))
        }
    }
}