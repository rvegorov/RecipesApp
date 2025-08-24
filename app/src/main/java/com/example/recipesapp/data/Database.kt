package com.example.recipesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.StoredRecipe

@Database(entities = [Category::class, StoredRecipe::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun recipesDao(): RecipesDao
}