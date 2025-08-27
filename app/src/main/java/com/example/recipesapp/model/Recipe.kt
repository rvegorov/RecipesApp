package com.example.recipesapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.recipesapp.data.AppTypeConverters
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity
@TypeConverters(AppTypeConverters::class)
data class Recipe(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "ingredients")
    val ingredients: List<Ingredient>,

    @ColumnInfo(name = "method")
    val method: List<String>,

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String,

    @ColumnInfo(name = "categoryId")
    var categoryId: Int? = null,

    @ColumnInfo(name = "isFavourite")
    var isFavourite: Boolean = false,
) : Parcelable
