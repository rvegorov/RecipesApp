package com.example.recipesapp.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Category

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {
    data class CategoriesListState(
        var categoriesList: List<Category>? = null
    )

    private val _state: MutableLiveData<CategoriesListState> =
        MutableLiveData<CategoriesListState>(CategoriesListState())
    val state: LiveData<CategoriesListState>
        get() {
            return _state
        }

    fun loadCategoriesList() {
        _state.value?.categoriesList = STUB.getCategories()
    }
}