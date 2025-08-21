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

class RecipesRepository(application: Application) {

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

    val database = Room.databaseBuilder(
        application.applicationContext,
        Database::class.java,
        name = "database-categories"
    ).build()
    val categoryDao = database.categoryDao()

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