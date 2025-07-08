package com.example.recipesapp.ui.recipes.recipe

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.recipesapp.ARG_RECIPE
import com.example.recipesapp.FAVOURITES_IDS_KEY
import com.example.recipesapp.R
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.SP_NAME
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

        val recipeStateObserver = Observer<RecipeState> {
            Log.i("!!!", recipeViewModel.state.value?.isFavourite.toString())
        }
        recipeViewModel.state.observe(viewLifecycleOwner, recipeStateObserver)

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

    fun initUI(recipe: Recipe) {
        binding.tvRecipeTitle.text = recipe.title
        try {
            val inputStream = binding.ivRecipeHeader.context?.assets?.open(recipe.imageUrl)
            val imageDrawable = Drawable.createFromStream(inputStream, recipe.imageUrl)
            binding.ivRecipeHeader.setImageDrawable(imageDrawable)
        } catch (e: Exception) {
            Log.e("assets", e.stackTraceToString())
        }

        val favouritesSet = getFavourites()
        if (favouritesSet.contains(recipe.id.toString())) {
            setFilledFavouriteIcon()
        } else {
            setEmptyFavouriteIcon()
        }

        binding.favouriteButton.setOnClickListener {
            if (favouritesSet.contains(recipe.id.toString())) {
                setEmptyFavouriteIcon()
                favouritesSet.remove(recipe.id.toString())
            } else {
                setFilledFavouriteIcon()
                favouritesSet.add(recipe.id.toString())
            }
            saveFavourites(favouritesSet)
        }
    }

    fun saveFavourites(ids: Set<String>) {
        val sharedPrefs = context?.getSharedPreferences(SP_NAME, MODE_PRIVATE)
        sharedPrefs?.edit {
            putStringSet(FAVOURITES_IDS_KEY, ids)
            apply()
        }
    }

    fun getFavourites(): MutableSet<String> {
        val sharedPrefs = context?.getSharedPreferences(SP_NAME, MODE_PRIVATE)
        val favouritesSet = sharedPrefs?.getStringSet(
            FAVOURITES_IDS_KEY,
            mutableSetOf()
        ) as MutableSet<String>
        return HashSet<String>(favouritesSet)
    }

    fun setEmptyFavouriteIcon() {
        binding.favouriteButton.setBackgroundResource(R.drawable.ic_heart_empty)
    }

    fun setFilledFavouriteIcon() {
        binding.favouriteButton.setBackgroundResource(R.drawable.ic_heart)
    }
}