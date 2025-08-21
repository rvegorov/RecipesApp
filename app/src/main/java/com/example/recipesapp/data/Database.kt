package com.example.recipesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipesapp.model.Category

@Database(entities = [Category::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}