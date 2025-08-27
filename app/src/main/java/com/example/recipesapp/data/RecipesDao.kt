package com.example.recipesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipesapp.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * from recipe WHERE id = :id")
    fun getRecipeById(id: Int): Recipe

    @Query("SELECT * from recipe WHERE id IN (:recipesIdsSet)")
    fun getRecipesByIds(recipesIdsSet: Set<Int>): List<Recipe>

    @Query("SELECT * from recipe WHERE categoryId = :categoryId")
    fun getRecipesByCategoryId(categoryId: Int): List<Recipe>

    @Query("SELECT * from recipe WHERE isFavourite = 1")
    fun getFavouriteRecipes(): List<Recipe>

    @Query("SELECT id from recipe WHERE isFavourite = 1")
    fun getFavouriteIds(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecipe(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecipeList(recipes: List<Recipe>)
}