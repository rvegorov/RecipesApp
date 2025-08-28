package com.example.recipesapp.data

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.recipesapp.API_URL
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RecipesRepository {
    lateinit var categoryDao: CategoryDao
    lateinit var recipesDao: RecipesDao

    val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val client = OkHttpClient.Builder().addInterceptor(logging).build()

    val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(API_URL)
        .addConverterFactory(
            Json.asConverterFactory(
                "application/json; charset=UTF8".toMediaType()
            )
        )
        .build()

    val apiService: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    fun init(application: Application) {

        val database = Room.databaseBuilder(
            application.applicationContext,
            Database::class.java,
            name = "database"
        ).build()

        categoryDao = database.categoryDao()
        recipesDao = database.recipesDao()
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        return try {
            withContext(Dispatchers.IO) {
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
            withContext(Dispatchers.IO) {
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
            withContext(Dispatchers.IO) {
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
            withContext(Dispatchers.IO) {
                recipesDao.getRecipesByIds(recipesIdsSet)
            }
        } catch (e: Exception) {
            Log.i("Repository", "${e.message}")
            null
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return try {
            withContext(Dispatchers.IO) {
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
