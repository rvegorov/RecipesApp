package com.example.recipesapp.ui.recipes.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.recipesapp.R
import com.example.recipesapp.ui.recipes.recipe.RecipeViewModel.RecipeState

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not to be null")

    val recipeViewModel: RecipeViewModel by viewModels()

    val recipeArgs: RecipeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(layoutInflater)

        return binding.root
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeId = recipeArgs.recipeId
        recipeViewModel.loadRecipe(recipeId)

        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initUI() {
        val ingredientsAdapter = IngredientsAdapter(null)
        val methodAdapter = MethodAdapter(null)

        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvMethod.adapter = methodAdapter

        val context = binding.rvIngredients.context
        val recyclerDivider =
            MaterialDividerItemDecoration(context, MaterialDividerItemDecoration.VERTICAL)
        recyclerDivider.setDividerColorResource(context, R.color.light_grey_color)
        recyclerDivider.isLastItemDecorated = false
        binding.rvIngredients.addItemDecoration(recyclerDivider)
        binding.rvMethod.addItemDecoration(recyclerDivider)

        val seekBarView = binding.sbServings
        val portionSeekBarListener = PortionSeekBarListener {
            recipeViewModel.setServings(seekBarView.progress)
        }

        val recipeStateObserver = Observer<RecipeState> {
            val recipe = it.recipe

            // Recipe UI
            binding.tvRecipeTitle.text = it.recipe?.title
            binding.ivRecipeHeader.setImageDrawable(it.recipeImage)
            binding.favouriteButton.setOnClickListener {
                recipeViewModel.onFavoritesClicked()
            }
            setFavouriteIcon(it.isFavourite)

            // Recycler
            recipe?.run {
                ingredientsAdapter.dataset = this.ingredients
                methodAdapter.methodList = this.method
                ingredientsAdapter.updateIngredients(it.servingsCount)
                ingredientsAdapter.notifyItemRangeChanged(0, ingredientsAdapter.itemCount)
                binding.tvRecipeServings.text = it.servingsCount.toString()

                seekBarView.setOnSeekBarChangeListener(portionSeekBarListener)
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

    class PortionSeekBarListener(
        val onChangeIngredients: (Int) -> Unit
    ) : OnSeekBarChangeListener {
        override fun onProgressChanged(
            seekBar: SeekBar?, progress: Int, fromUser: Boolean
        ) {
            onChangeIngredients(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    }
}