package com.example.recipeapp

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.recipeapp.databinding.ActivityRecipeBinding

class RecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeBinding
    var imgCrop = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load image safely
        val imgUrl = intent.getStringExtra("img")
        if (!imgUrl.isNullOrEmpty()) {
            Glide.with(this).load(imgUrl).into(binding.itemImage)
        }

        binding.tittle.text = intent.getStringExtra("tittle")
        binding.stepData.text = intent.getStringExtra("des")

        // Fixing the toRegex() error by ensuring a non-null String
        val ing = intent.getStringExtra("ing")
            ?.orEmpty()   // Ensures it's a non-null String
            ?.split("\n")
            ?.dropLastWhile { it.isEmpty() }
            ?.toTypedArray()

        // Handle null or empty ingredient list safely
        if (!ing.isNullOrEmpty()) {
            binding.time.text = ing[0]
            val ingredientsText = StringBuilder()
            for (i in 1 until ing.size) {
                ingredientsText.append("🟢 ${ing[i]}\n")
            }
            binding.ingData.text = ingredientsText.trim()
        }
        binding.step.background=null
        binding.step.setTextColor((getColor(R.color.black)))

        binding.step.setOnClickListener {
            binding.step.setBackgroundResource(R.drawable.btn_ing)
            binding.step.setTextColor((getColor(R.color.white)))
            binding.ing.setTextColor((getColor(R.color.black)))
            binding.ing.background=null
            binding.stepScroll.visibility = View.VISIBLE
            binding.ingScroll.visibility = View.GONE
        }

        binding.ing.setOnClickListener {
            binding.ing.setBackgroundResource(R.drawable.btn_ing)
            binding.ing.setTextColor((getColor(R.color.white)))
            binding.step.setTextColor((getColor(R.color.black)))
            binding.step.background = null
            binding.ingScroll.visibility = View.VISIBLE
            binding.stepScroll.visibility = View.GONE
        }

        // Toggle image full screen mode
        binding.fullScreen.setOnClickListener {
            imgCrop = if (imgCrop) {
                binding.itemImage.scaleType = ImageView.ScaleType.FIT_CENTER
                binding.fullScreen.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
                false
            } else {
                binding.itemImage.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.fullScreen.setColorFilter(null)
                true
            }

            // Reload the image only when necessary
            if (!imgUrl.isNullOrEmpty()) {
                Glide.with(this).load(imgUrl).into(binding.itemImage)
            }

            binding.shade.visibility = View.VISIBLE
        }

        // Back button click handler
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}
