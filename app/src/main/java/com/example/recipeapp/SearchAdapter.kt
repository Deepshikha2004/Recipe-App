package com.example.recipeapp

import android.app.appsearch.AppSearchManager.SearchContext
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.databinding.SearchRvBinding

class SearchAdapter(var dataList: ArrayList<Recipe>,var context: Context):RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: SearchRvBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view=SearchRvBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    fun filterList(filteredList: ArrayList<Recipe>) {
        dataList = filteredList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(dataList.get(position).img).into(holder.binding.searchImg)
        holder.binding.searchTxt.text=dataList.get(position).tittle
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