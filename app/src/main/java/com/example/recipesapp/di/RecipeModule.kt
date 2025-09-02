package com.example.recipesapp.di

import android.content.Context
import androidx.room.Room
import com.example.recipesapp.API_URL
import com.example.recipesapp.data.CategoryDao
import com.example.recipesapp.data.Database
import com.example.recipesapp.data.RecipeApiService
import com.example.recipesapp.data.RecipesDao
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RecipeModule() {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            name = "database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideCategoryDao(database: Database): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideRecipesDao(database: Database): RecipesDao {
        return database.recipesDao()
    }

    @Provides
    fun provideHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(logging).build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(API_URL)
            .addConverterFactory(
                Json.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()
                )
            )
            .build()
    }

    @Provides
    fun RecipeApiService(retrofit: Retrofit): RecipeApiService {
        return retrofit.create(RecipeApiService::class.java)
    }
}