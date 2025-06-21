package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipesapp.databinding.FragmentListRecipesBinding

class RecipesListFragment : Fragment() {
    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListRecipesBinding must not to be null")
    private var _categoryId: Int? = null
    private var _categoryName: String? = null
    private var _categoryImageUrl: String? = null
    val categoryId
        get() = _categoryId
            ?: throw IllegalStateException("categoryId must not to be null")
    val categoryName
        get() = _categoryName
            ?: throw IllegalStateException("categoryName not to be null")
    val categoryImageUrl
        get() = _categoryImageUrl
            ?: "bcg_categories"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListRecipesBinding.inflate(layoutInflater)
        _categoryId = arguments?.getInt("ARG_CATEGORY_ID")
        _categoryName = arguments?.getString("ARG_CATEGORY_NAME")
        _categoryImageUrl = arguments?.getString("ARG_CATEGORY_IMAGE_URL")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvCategoryTitle.text = categoryName
        try {
            val inputStream = binding.ivCategoryHeader.context?.assets?.open(categoryImageUrl)
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
        val recipesListAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId(categoryId))
        recipesListAdapter.setOnItemClickListener( object :
            RecipesListAdapter.OnItemClickListener{
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            }
        )
        val recycler = binding.rvRecipes
        recycler.adapter = recipesListAdapter

    }

    fun openRecipeByRecipeId(recipeId: Int) {
        this.parentFragmentManager.commit {
            replace<RecipeFragment>(containerViewId = R.id.mainContainer)
            setReorderingAllowed(true)
        }
    }

}