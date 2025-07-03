package com.example.recipesapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ItemMethodBinding

class MethodAdapter(private val methodList: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemMethodBinding) : RecyclerView.ViewHolder(binding.root) {
        val textView = binding.root
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemMethodBinding.inflate(inflater, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        position: Int
    ) {
        viewHolder.textView.text = viewHolder.itemView.context.getString(
            R.string.recipe_method_string_format,
            position,
            methodList[position],
        )
    }

    override fun getItemCount(): Int {
        return methodList.size
    }
}