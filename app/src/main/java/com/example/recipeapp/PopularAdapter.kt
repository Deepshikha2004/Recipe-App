package com.example.recipeapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.databinding.PopularRvItemBinding

class PopularAdapter(
    var dataList: ArrayList<Recipe>,
    var context: Context
) : RecyclerView.Adapter<PopularAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: PopularRvItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PopularRvItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = dataList[position]

        // ✅ Load the image with Glide and placeholder handling
        Glide.with(context)
            .load(recipe.img)
            .placeholder(R.drawable.category_salad)  // Add placeholder for smoother loading
            .into(holder.binding.popularImg)

        // Set text values
        holder.binding.popularTxt.text = recipe.tittle

        // Displaying preparation time properly
        val time = recipe.ing.split("\n").dropLastWhile { it.isEmpty() }
        holder.binding.popularTime.text = if (time.isNotEmpty()) time[0] else "N/A"
        holder.itemView.setOnClickListener {
            var intent = Intent(context, RecipeActivity::class.java)
            intent.putExtra("img",dataList.get(position).img)
            intent.putExtra("tittle",dataList.get(position).tittle)
            intent.putExtra("des",dataList.get(position).des)
            intent.putExtra("ing",dataList.get(position).ing)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}
