package com.example.recipeapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.recipeapp.databinding.ActivityCategoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryActivity : AppCompatActivity() {
    private lateinit var rvAdapter: CategoryAdapter
    private lateinit var dataList: ArrayList<Recipe>
    private val binding by lazy {
        ActivityCategoryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = intent.getStringExtra("TITTLE")

        // Use CoroutineScope for database operations
        CoroutineScope(Dispatchers.Main).launch {
            setUpRecyclerView()
        }

        binding.goBackHome.setOnClickListener {
            finish()
        }
    }

    private suspend fun setUpRecyclerView() {
        dataList = ArrayList()
        binding.rvCategory.layoutManager = LinearLayoutManager(this)

        try {
            // ✅ Correct database path
            val db = withContext(Dispatchers.IO) {
                Room.databaseBuilder(
                    this@CategoryActivity,
                    AppDatabase::class.java,
                    "db_name"
                )
                    .fallbackToDestructiveMigration()
                    .createFromAsset("recipe.db")  // ✅ Correct path
                    .build()
            }

            val recipes = withContext(Dispatchers.IO) { db.recipeDao().getAllRecipes() }

            // ✅ Log to check the data size
            Log.d("RecipeCount", "Recipes found: ${recipes.size}")

            for (recipe in recipes) {
                if (recipe.category.contains(intent.getStringExtra("CATEGORY") ?: "")) {
                    dataList.add(recipe)
                }
            }

            if (dataList.isEmpty()) {
                Log.d("RecyclerView", "No data found for the category")
            }

            // Set adapter only when data is ready
            rvAdapter = CategoryAdapter(dataList, this@CategoryActivity)
            binding.rvCategory.adapter = rvAdapter

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("DB_ERROR", "Error: ${e.message}")
        }
    }
}
