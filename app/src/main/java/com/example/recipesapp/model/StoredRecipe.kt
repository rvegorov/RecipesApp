package com.example.recipesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recipe")
data class StoredRecipe(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "ingredients") val ingredients: String,
    @ColumnInfo(name = "method") val method: String,
    @ColumnInfo(name = "mageUrl") val imageUrl: String,
    @ColumnInfo(name = "categoryId") var categoryId: Int? = null,
)