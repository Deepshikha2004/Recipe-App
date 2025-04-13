package com.example.recipeapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.recipeapp.databinding.ActivityHomeBinding
import androidx.lifecycle.lifecycleScope  // ✅ Import lifecycleScope
import kotlinx.coroutines.launch         // ✅ Import launch for coroutine

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var rvAdapter: PopularAdapter
    private lateinit var dataList: ArrayList<Recipe>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ Launch the suspend function inside a coroutine
        lifecycleScope.launch {
            setUpRecyclerView()
            binding.editTextText.setOnClickListener {
                startActivity(Intent(this@HomeActivity, SearchActivity::class.java))
            }
        }
        binding.salad.setOnClickListener{
            var myIntent = Intent(this@HomeActivity, CategoryActivity::class.java)
            myIntent.putExtra("TITTlE", "Salad")
            myIntent.putExtra("CATEGORY", "Salad")
            startActivity(myIntent)
        }
        binding.mainDish.setOnClickListener{
            var myIntent = Intent(this@HomeActivity, CategoryActivity::class.java)
            myIntent.putExtra("TITTlE", "Main Dish")
            myIntent.putExtra("CATEGORY", "Dish")
            startActivity(myIntent)
        }
        binding.drinks.setOnClickListener{
            var myIntent = Intent(this@HomeActivity, CategoryActivity::class.java)
            myIntent.putExtra("TITTlE", "Drinks")
            myIntent.putExtra("CATEGORY", "Drinks")
            startActivity(myIntent)
        }
        binding.desserts.setOnClickListener{
            var myIntent = Intent(this@HomeActivity, CategoryActivity::class.java)
            myIntent.putExtra("TITTlE", "Desserts")
            myIntent.putExtra("CATEGORY", "Desserts")
            startActivity(myIntent)
        }
    }

    private suspend fun setUpRecyclerView() {
        dataList = ArrayList()
        binding.rvPopular.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        try {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "db_name"
            )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .createFromAsset("recipe.db")  // ✅ Correct path
                .build()

            val recipes = db.recipeDao().getAllRecipes()

            for (recipe in recipes) {
                if (recipe.category.contains("Popular")) {
                    dataList.add(recipe)
                }
            }

            rvAdapter = PopularAdapter(dataList, this@HomeActivity)
            binding.rvPopular.adapter = rvAdapter

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
