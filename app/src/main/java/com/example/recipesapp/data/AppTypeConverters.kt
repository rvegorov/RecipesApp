package com.example.recipesapp.data

import androidx.room.TypeConverter
import com.example.recipesapp.model.Ingredient
import kotlinx.serialization.json.Json


class AppTypeConverters {
    @TypeConverter
    fun ingredientsListToString(ingredients: List<Ingredient>): String {
        return Json.encodeToString(ingredients)
    }

    @TypeConverter
    fun stringToIngredientsList(string: String): List<Ingredient> {
        return Json.decodeFromString(string)
    }

    @TypeConverter
    fun stringListToString(listOfStrings: List<String>): String {
        return Json.encodeToString(listOfStrings)
    }

    @TypeConverter
    fun stringToStringList(string: String): List<String> {
        return Json.decodeFromString(string)
    }
}