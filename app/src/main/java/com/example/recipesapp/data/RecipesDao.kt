package com.example.recipesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipesapp.model.StoredRecipe

@Dao
interface RecipesDao {
    @Query("SELECT * from recipe WHERE id = :id")
    fun getRecipeById(id: Int): StoredRecipe

    @Query("SELECT * from recipe WHERE id IN (:recipesIdsSet)")
    fun getRecipesByIds(recipesIdsSet: Set<Int>): List<StoredRecipe>

    @Query("SELECT * from recipe WHERE categoryId = :categoryId")
    fun getRecipesByCategoryId(categoryId: Int): List<StoredRecipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecipe(recipe: StoredRecipe)
}