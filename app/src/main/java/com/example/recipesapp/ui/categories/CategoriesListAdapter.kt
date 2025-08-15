package com.example.recipesapp.ui.categories

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.API_IMG_URL
import com.example.recipesapp.model.Category
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ItemCategoryBinding

class CategoriesListAdapter(var dataSet: List<Category>?) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.ivCategoryImage
        val titleTextView: TextView = binding.tvCategoryTitle
        val descriptionTextView: TextView = binding.tvCategoryDescription
        val cardView = binding.root
    }

    interface OnItemClickListener {
        fun onItemClick(category: Category)
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
        val view = ItemCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        position: Int
    ) {
        val category = dataSet?.get(position)
        category?.let {
            viewHolder.titleTextView.text = category.title
            viewHolder.descriptionTextView.text = category.description
            viewHolder.cardView.setOnClickListener(OnClickListener {
                itemClickListener?.onItemClick(category)
            })

            Glide.with(viewHolder.cardView)
                .load("$API_IMG_URL${category.imageUrl}")
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(viewHolder.imageView)

            viewHolder.imageView.contentDescription =
                viewHolder.imageView.context?.getString(R.string.description_category_image_placeholder) + " " + category.title
        }
    }

    override fun getItemCount(): Int {
        return dataSet?.size ?: 0
    }
}