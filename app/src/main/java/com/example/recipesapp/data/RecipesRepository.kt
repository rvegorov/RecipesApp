package com.example.recipesapp.data

import android.util.Log
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RecipesRepository @Inject constructor(
    val categoryDao: CategoryDao,
    val recipesDao: RecipesDao,
    val apiService: RecipeApiService,
) {

    val ioDispatcher: CoroutineContext = Dispatchers.IO

    val dataErrorText = "Ошибка получения данных"

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        return try {
            withContext(ioDispatcher) {
                apiService.getRecipeById(recipeId)
                    .execute()
                    .body()
            }

        } catch (e: Exception) {
            Log.i("Repository", "${e.message}")
            null
        }
    }

    suspend fun getRecipeByIdFromCache(recipeId: Int): Recipe? {
        return try {
            withContext(ioDispatcher) {
                recipesDao.getRecipeById(recipeId)
            }

        } catch (e: Exception) {
            Log.i("Repository", "${e.message}")
            null
        }
    }

    suspend fun getRecipesByIds(recipesIdsSet: Set<Int>): List<Recipe>? {
        val recipesIdsString = recipesIdsSet.joinToString(",")
        return try {
            withContext(ioDispatcher) {
                apiService.getRecipesByIds(recipesIdsString)
                    .execute()
                    .body()
            }
        } catch (e: Exception) {
            Log.i("Repository", "${e.message}")
            null
        }
    }

    suspend fun getRecipesByIdsFromCache(recipesIdsSet: Set<Int>): List<Recipe>? {
        return try {
            withContext(ioDispatcher) {
                recipesDao.getRecipesByIds(recipesIdsSet)
            }
        } catch (e: Exception) {
            Log.i("Repository", "${e.message}")
            null
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return try {
            withContext(ioDispatcher) {
                apiService.getRecipesByCategoryId(categoryId)
                    .execute()
                    .body()
            }
        } catch (e: Exception) {
            Log.i("Repository", "${e.message}")
            null
        }
    }

    suspend fun getRecipesByCategoryIdFromCache(categoryId: Int): List<Recipe>? {
        return try {
            withContext(Dispatchers.IO) {
                recipesDao.getRecipesByCategoryId(categoryId)
            }
        } catch (e: Exception) {
            Log.i("Repository", "${e.message}")
            null
        }
    }

    suspend fun getFavouriteRecipes(): List<Recipe>? {
        return try {
            withContext(Dispatchers.IO) {
                recipesDao.getFavouriteRecipes()
            }
        } catch (e: Exception) {
            Log.i("Repository", "${e.message}")
            null
        }
    }

    suspend fun getFavouriteIds(): List<Int>? {
        return try {
            withContext(Dispatchers.IO) {
                recipesDao.getFavouriteIds()
            }
        } catch (e: Exception) {
            Log.i("Repository", "${e.message}")
            null
        }
    }

    suspend fun addRecipe(recipe: Recipe, categoryId: Int) {
        recipe.categoryId = categoryId
        withContext(Dispatchers.IO) {
            recipesDao.addRecipe(recipe)
        }
    }

    suspend fun addRecipeList(recipes: List<Recipe>, categoryId: Int, favouritesList: List<Int>?) {
        recipes.forEach { recipe ->
            if (favouritesList?.contains(recipe.id) == true) {
                recipe.isFavourite = true
            }
            recipe.categoryId = categoryId
        }
        withContext(Dispatchers.IO) {
            recipesDao.addRecipeList(recipes)
        }
    }

    suspend fun getCategories(): List<Category>? {
        return try {
            withContext(Dispatchers.IO) {
                apiService.getCategories()
                    .execute().body()
            }
        } catch (e: Exception) {
            Log.i("Repository", "${e.message}")
            null
        }
    }

    suspend fun getCategoriesFromCache(): List<Category>? {
        return try {
            withContext(Dispatchers.IO) {
                categoryDao.getCategoriesList()
            }
        } catch (e: Exception) {
            Log.e("!!!", e.message.toString())
            null
        }
    }

    suspend fun addCategory(category: Category) {
        withContext(Dispatchers.IO) {
            categoryDao.addCategory(category)
        }
    }
}
