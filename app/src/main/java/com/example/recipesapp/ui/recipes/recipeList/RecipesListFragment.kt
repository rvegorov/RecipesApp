package com.example.recipesapp.ui.recipes.recipeList

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipesapp.ARG_CATEGORY_ID
import com.example.recipesapp.ARG_CATEGORY_IMAGE_URL
import com.example.recipesapp.ARG_CATEGORY_NAME
import com.example.recipesapp.ARG_RECIPE_ID
import com.example.recipesapp.R
import com.example.recipesapp.data.STUB
import com.example.recipesapp.databinding.FragmentListRecipesBinding
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment

class RecipesListFragment : Fragment() {
    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListRecipesBinding must not to be null")
    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListRecipesBinding.inflate(layoutInflater)
        categoryId = arguments?.getInt(ARG_CATEGORY_ID)
        categoryName = arguments?.getString(ARG_CATEGORY_NAME)
        categoryImageUrl = arguments?.getString(ARG_CATEGORY_IMAGE_URL)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvCategoryTitle.text = categoryName
        try {
            val inputStream = binding.ivCategoryHeader.context?.assets?.open(categoryImageUrl ?: "")
            val imageDrawable = Drawable.createFromStream(inputStream, categoryImageUrl)
            binding.ivCategoryHeader.setImageDrawable(imageDrawable)
        } catch (e: Exception) {
            Log.e("assets", e.stackTraceToString())
        }
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun initRecycler() {
        val recipesListAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId(categoryId ?: 0))
        recipesListAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        }
        )
        val recycler = binding.rvRecipes
        recycler.adapter = recipesListAdapter

    }

    fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = Bundle().apply {
            putInt(ARG_RECIPE_ID, recipeId)
        }

        this.parentFragmentManager.commit {
            replace<RecipeFragment>(containerViewId = R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
        }
    }
}