package com.example.recipesapp.ui.recipes.recipeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.recipesapp.ARG_CATEGORY_ID
import com.example.recipesapp.ARG_RECIPE_ID
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentListRecipesBinding
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment
import com.example.recipesapp.ui.recipes.recipeList.RecipesListViewModel.RecipesListState

class RecipesListFragment : Fragment() {
    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListRecipesBinding must not to be null")
    private var categoryId: Int? = null
    private val recipesListViewModel: RecipesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListRecipesBinding.inflate(layoutInflater)
        categoryId = arguments?.getInt(ARG_CATEGORY_ID)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipesListViewModel.loadRecipesList(categoryId)
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun initUI() {
        val recipesListAdapter = RecipesListAdapter(null)
        binding.rvRecipes.adapter = recipesListAdapter

        val recipesListStateObserver = Observer<RecipesListState> {
            // Category UI
            binding.tvCategoryTitle.text = it.category?.title
            binding.ivCategoryHeader.setImageDrawable(it.categoryImage)

            // Recycler
            recipesListAdapter.dataSet = it.recipesList
            recipesListAdapter.setOnItemClickListener(object :
                RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            })
        }
        recipesListViewModel.state.observe(viewLifecycleOwner, recipesListStateObserver)
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