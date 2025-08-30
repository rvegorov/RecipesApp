package com.example.recipesapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Category
import kotlinx.coroutines.launch

class CategoriesListViewModel(val repository: RecipesRepository) : ViewModel() {
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


    fun loadCategoriesList() {
        viewModelScope.launch {
            val categoriesListCached = repository.getCategoriesFromCache()
            if (!categoriesListCached.isNullOrEmpty()) {
                _state.postValue(CategoriesListState(categoriesList = categoriesListCached))
            }

            val categoriesList = repository.getCategories()
            if (categoriesList == null) {
                _uiMessage.value = UiMessage(message = repository.dataErrorText)
                _uiMessage.value = UiMessage(message = repository.dataErrorText)
            } else {
                categoriesList.forEach { category ->
                    repository.addCategory(category)
                }
                _state.postValue(CategoriesListState(categoriesList = categoriesList))
            }

        }
    }
}