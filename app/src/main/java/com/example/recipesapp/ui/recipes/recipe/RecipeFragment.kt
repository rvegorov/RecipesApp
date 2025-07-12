package com.example.recipesapp.ui.recipes.recipe

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.recipesapp.ARG_RECIPE_ID
import com.example.recipesapp.R
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.ui.recipes.recipe.RecipeViewModel.RecipeState

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not to be null")

    val recipeViewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(layoutInflater)

        return binding.root
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeId = arguments?.getInt(ARG_RECIPE_ID)
        recipeViewModel.loadRecipe(recipeId)

        initUI()
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

        val seekBarView = binding.sbServings
        seekBarView.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                ingredientsAdapter.updateIngredients(progress)
                ingredientsAdapter.notifyDataSetChanged()
                binding.tvRecipeServings.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    fun initUI() {
        val recipeStateObserver = Observer<RecipeState> {
            Log.i("!!!", "#${it.recipe?.id} is in favourite: ${it.isFavourite}")
            val recipe = it.recipe
            binding.tvRecipeTitle.text = it.recipe?.title
            try {
                val inputStream =
                    binding.ivRecipeHeader.context?.assets?.open(recipe?.imageUrl as String)
                val imageDrawable = Drawable.createFromStream(inputStream, recipe?.imageUrl)
                binding.ivRecipeHeader.setImageDrawable(imageDrawable)
            } catch (e: Exception) {
                Log.e("assets", e.stackTraceToString())
            }
            binding.favouriteButton.setOnClickListener {
                recipeViewModel.onFavoritesClicked()
            }
            setFavouriteIcon(it.isFavourite)
            recipe?.let {
                initRecycler(it)
            }
        }

        recipeViewModel.state.observe(viewLifecycleOwner, recipeStateObserver)
    }

    fun setFavouriteIcon(isFavourite: Boolean) {
        if (isFavourite) {
            binding.favouriteButton.setBackgroundResource(R.drawable.ic_heart)
        } else {
            binding.favouriteButton.setBackgroundResource(R.drawable.ic_heart_empty)
        }
    }
}