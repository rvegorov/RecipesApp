package com.example.recipesapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.model.Ingredient
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ItemIngredientBinding
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter(
    private val dataset: List<Ingredient>
) : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    var quantity: Int = 1

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

        var quantityString = BigDecimal(data.quantity)
            .times(BigDecimal(quantity))
            .setScale(1, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()

        viewHolder.ingredientNameView.text = data.description
        viewHolder.ingredientQuantityView.text =
            viewHolder.itemView.context.getString(
                R.string.recipe_ingredient_string_format,
                quantityString,
                data.unitOfMeasure
            )
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun updateIngredients(progress: Int) {
        quantity = progress
    }
}