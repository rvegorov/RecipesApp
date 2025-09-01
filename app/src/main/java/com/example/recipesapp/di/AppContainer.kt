package com.example.recipesapp.di

import android.content.Context
import androidx.room.Room
import com.example.recipesapp.API_URL
import com.example.recipesapp.data.Database
import com.example.recipesapp.data.RecipeApiService
import com.example.recipesapp.data.RecipesRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class AppContainer(context: Context) {
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
        context,
        Database::class.java,
        name = "database"
    ).build()

    val categoryDao = database.categoryDao()
    val recipesDao = database.recipesDao()

    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    val repository = RecipesRepository(
        categoryDao = categoryDao,
        recipesDao = recipesDao,
        apiService = apiService,
        ioDispatcher = ioDispatcher,
    )

    val categoriesListViewModelFactory = CategoriesListViewModelFactory(repository)
    val favouritesViewModelFactory = FavouritesViewModelFactory(repository)
    val recipesListViewModelFactory = RecipesListViewModelFactory(repository)
    val recipeViewModelFactory = RecipeViewModelFactory(repository)
}