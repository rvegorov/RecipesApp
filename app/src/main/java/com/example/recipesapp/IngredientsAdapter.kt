package com.example.recipesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemIngredientBinding

class IngredientsAdapter(
    private val dataset: List<Ingredient>
) : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root) {
        val ingredientNameView = binding.tvIngredientName
        val ingredientQuantityView = binding.tvIngredientQuantity
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemIngredientBinding.inflate(inflater, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        position: Int
    ) {
        val data = dataset[position]
        viewHolder.ingredientNameView.text = data.description
        val stringFormat =
            viewHolder.ingredientNameView.context.getString(R.string.recipe_ingredient_string_format)
        viewHolder.ingredientQuantityView.text =
            String.format(stringFormat, data.quantity, data.unitOfMeasure)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}