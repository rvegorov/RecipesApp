package com.example.recipesapp.data

import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    @GET("recipe/{id}")
    fun getRecipeById(@Path("id") recipeId: Int): Call<Recipe>

    @GET("recipes")
    fun getRecipesByIds(@Query("ids") recipesIdsString: String): Call<List<Recipe>>

    @GET("category/{id}")
    fun getCategoryById(@Path("id") categoryId: Int): Call<Category>

    @GET("category/{id}/recipes")
    fun getRecipesByCategoryId(@Path("id") categoryId: Int): Call<List<Recipe>>

    @GET("category")
    fun getCategories(): Call<List<Category>>
}