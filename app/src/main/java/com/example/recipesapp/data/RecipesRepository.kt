package com.example.recipesapp.data

import android.util.Log
import com.example.recipesapp.API_URL
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class RecipesRepository {

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

    fun getRecipeById(recipeId: Int): Recipe? {
        return try {
            apiService.getRecipeById(recipeId)
                .execute()
                .body()
        } catch (e: Exception) {
            Log.i("Repository", "${e.message}")
            null
        }
    }

    fun getRecipesByIds(recipesIdsSet: Set<Int>): List<Recipe>? {
        val recipesIdsString = recipesIdsSet.joinToString(",")
        return try {
            apiService.getRecipesByIds(recipesIdsString)
                .execute()
                .body()
        } catch (e: Exception) {
            Log.i("Repository", "${e.message}")
            null
        }
    }

    fun getCategoryById(categoryId: Int): Category? {
        return try {
            apiService.getCategoryById(categoryId)
                .execute()
                .body()
        } catch (e: Exception) {
            Log.i("Repository", "${e.message}")
            null
        }
    }

    fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return try {
            apiService.getRecipesByCategoryId(categoryId)
                .execute()
                .body()
        } catch (e: Exception) {
            Log.i("Repository", "${e.message}")
            null
        }
    }

    fun getCategories(): List<Category>? {
        return try {
            apiService.getCategories()
                .execute().body()
        } catch (e: Exception) {
            Log.i("Repository", "${e.message}")
            null
        }
    }
}