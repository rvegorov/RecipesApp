package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not to be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {
            arguments?.getParcelable<Recipe>(ARG_RECIPE)
        }
        recipe?.let {
            initRecycler(it)
            initUI(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun initRecycler(recipe: Recipe) {
        val context = binding.rvIngredients.context
        val recyclerDivider =
            MaterialDividerItemDecoration(context, MaterialDividerItemDecoration.VERTICAL)
        recyclerDivider.setDividerColorResource(context, R.color.light_grey_color)
        recyclerDivider.isLastItemDecorated = false
        binding.rvIngredients.addItemDecoration(recyclerDivider)
        binding.rvMethod.addItemDecoration(recyclerDivider)
        val ingredientsAdapter = IngredientsAdapter(recipe.ingredients)
        binding.rvIngredients.adapter = ingredientsAdapter
        val methodAdapter = MethodAdapter(recipe.method)
        binding.rvMethod.adapter = methodAdapter
    }

    fun initUI(recipe: Recipe) {
        binding.tvRecipeTitle.text = recipe.title
        try {
            val inputStream = binding.ivRecipeHeader.context?.assets?.open(recipe.imageUrl)
            val imageDrawable = Drawable.createFromStream(inputStream, recipe.imageUrl)
            binding.ivRecipeHeader.setImageDrawable(imageDrawable)
        } catch (e: Exception) {
            Log.e("assets", e.stackTraceToString())
        }
    }
}