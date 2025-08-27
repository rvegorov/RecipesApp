package com.example.recipesapp.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.R
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Category
import kotlinx.coroutines.launch

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {
    data class CategoriesListState(
        var categoriesList: List<Category>? = null
    )

    data class UiMessage(
        var message: String? = null
    )

    private val _state: MutableLiveData<CategoriesListState> =
        MutableLiveData<CategoriesListState>(CategoriesListState())
    val state: LiveData<CategoriesListState>
        get() {
            return _state
        }

    private val _uiMessage: MutableLiveData<UiMessage> = MutableLiveData<UiMessage>(UiMessage())
    val uiMessage: LiveData<UiMessage>
        get() {
            return _uiMessage
        }

    val context = getApplication<Application>()

    fun loadCategoriesList() {
        viewModelScope.launch {
            val categoriesListCached = RecipesRepository.getCategoriesFromCache()
            if (!categoriesListCached.isNullOrEmpty()) {
                _state.postValue(CategoriesListState(categoriesList = categoriesListCached))
            }

            val categoriesList = RecipesRepository.getCategories()
            if (categoriesList == null) {
                _uiMessage.value = UiMessage(message = context.getString(R.string.dataError))
            } else {
                categoriesList.forEach { category ->
                    RecipesRepository.addCategory(category)
                }
                _state.postValue(CategoriesListState(categoriesList = categoriesList))
            }

        }
    }
}