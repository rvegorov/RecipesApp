package com.example.recipesapp.ui.categories

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        val data = dataSet?.get(position)
        data?.let {
            viewHolder.titleTextView.text = data.title
            viewHolder.descriptionTextView.text = data.description
            viewHolder.cardView.setOnClickListener(OnClickListener {
                itemClickListener?.onItemClick(data)
            })
            try {
                val inputStream = viewHolder.imageView.context?.assets?.open(data.imageUrl)
                val imageDrawable = Drawable.createFromStream(inputStream, data.imageUrl)
                viewHolder.imageView.setImageDrawable(imageDrawable)
            } catch (e: Exception) {
                Log.e("assets", e.stackTraceToString())
            }
            viewHolder.imageView.contentDescription =
                viewHolder.imageView.context?.getString(R.string.description_category_image_placeholder) + " " + data.title
        }
    }

    override fun getItemCount(): Int {
        return dataSet?.size ?: 0
    }
}