package com.example.recipesapp.ui.recipes.recipeList

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.API_IMG_URL
import com.example.recipesapp.R
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.databinding.ItemRecipeBinding

class RecipesListAdapter(var dataSet: List<Recipe>?) :
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
        val recipe = dataSet?.get(position)
        recipe?.run {
            viewHolder.titleTextView.text = recipe.title

            Glide.with(viewHolder.cardView)
                .load("$API_IMG_URL${recipe.imageUrl}")
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(viewHolder.imageView)

            viewHolder.imageView.contentDescription =
                viewHolder.imageView.context?.getString(R.string.description_recipe_image_placeholder) + " " + recipe.title

            viewHolder.cardView.setOnClickListener(OnClickListener {
                itemClickListener?.onItemClick(recipe.id)
            })
        }
    }

    override fun getItemCount(): Int {
        return dataSet?.size ?: 0
    }
}