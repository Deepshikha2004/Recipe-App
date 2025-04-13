package com.example.recipeapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.recipeapp.databinding.ActivitySearchBinding
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var rvAdapter: SearchAdapter
    private lateinit var dataList: ArrayList<Recipe>
    private lateinit var recipes: ArrayList<Recipe>  // Ensure it's ArrayList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.search.requestFocus()

        val db = Room.databaseBuilder(
            this@SearchActivity,
            AppDatabase::class.java,
            "db_name"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .createFromAsset("recipe.db")
            .build()

        val daoObject = db.recipeDao()

        // Use coroutine to fetch recipes
        lifecycleScope.launch {
            recipes = ArrayList(daoObject.getAllRecipes())  // Convert List to ArrayList
            setUpRecyclerView()
        }
        binding.goBackHome.setOnClickListener{
            finish()
        }

        // Add text change listener for search
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    filterData(s.toString())
                } else {
                    rvAdapter.filterList(recipes)  // Pass ArrayList
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Hide keyboard when touching RecyclerView
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        binding.rvSearch.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                currentFocus?.clearFocus()
            }
            false
        }
    }

    // Filter function
    private fun filterData(query: String) {
        val filteredList = ArrayList<Recipe>()

        for (recipe in recipes) {
            if (recipe.tittle.lowercase().contains(query.lowercase())) {
                filteredList.add(recipe)
            }
        }

        rvAdapter.filterList(filteredList)
    }

    // Suspend function to load data
    private suspend fun setUpRecyclerView() {
        dataList = ArrayList()
        binding.rvSearch.layoutManager = LinearLayoutManager(this)

        // Filter only "Popular" recipes
        for (recipe in recipes) {
            if (recipe.category.contains("Popular")) {
                dataList.add(recipe)
            }
        }

        rvAdapter = SearchAdapter(dataList, this)
        binding.rvSearch.adapter = rvAdapter
    }
}
