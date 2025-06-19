package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemRecipeBinding

class RecipesListAdapter(private val dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {
    class ViewHolder(binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.ivRecipeImage
        val titleTextView: TextView = binding.tvRecipeTitle
        val cardView = binding.root
    }

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemRecipeBinding.inflate(inflater, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        position: Int
    ) {
        val data = dataSet[position]
        viewHolder.titleTextView.text = data.title
        try {
            val inputStream = viewHolder.imageView.context?.assets?.open(data.imageUrl)
            val imageDrawable = Drawable.createFromStream(inputStream, data.imageUrl)
            viewHolder.imageView.setImageDrawable(imageDrawable)
            viewHolder.cardView.setOnClickListener(OnClickListener {
                itemClickListener?.onItemClick(data.id)
            })
        } catch (e: Exception) {
            Log.e("assets", e.stackTraceToString())
        }
        viewHolder.imageView.contentDescription =
            viewHolder.imageView.context?.getString(R.string.description_recipe_image_placeholder) + " " + data.title
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}