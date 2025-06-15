package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemCategoryBinding

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {
    class ViewHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.ivCategoryImage
        val titleTextView: TextView = binding.tvCategoryTitle
        val descriptionTextView: TextView = binding.tvCategoryDescription

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        position: Int
    ) {
        val data = dataSet[position]
        viewHolder.titleTextView.text = data.title
        viewHolder.descriptionTextView.text = data.description
        try {
            val inputStream = viewHolder.imageView.context?.assets?.open(data.imageUrl)
            val imageDrawable = Drawable.createFromStream(inputStream, data.imageUrl)
            viewHolder.imageView.setImageDrawable(imageDrawable)
        } catch (e: Exception) {
            Log.e("assets", e.stackTraceToString())
        }
        viewHolder.imageView.contentDescription =
            viewHolder.imageView.context?.
            getString(R.string.description_category_image_placeholder) + " " + data.title
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}