package com.example.recipeapp

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipeapp.Recipe         // ✅ Correct import
import com.example.recipeapp.RecipeDao      // ✅ Correct import path

@Database(entities = [Recipe::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao     // ✅ Consistent naming
}
