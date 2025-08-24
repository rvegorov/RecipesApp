package com.example.recipesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipesapp.model.Category

@Dao
interface CategoryDao {
    @Query("SELECT * from category")
    fun getCategoriesList(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategory(category: Category)
}