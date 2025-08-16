package com.example.recipesapp.ui.recipes.recipeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipesapp.databinding.FragmentListRecipesBinding
import com.example.recipesapp.model.Category
import com.example.recipesapp.ui.recipes.recipeList.RecipesListViewModel.RecipesListState
import com.example.recipesapp.R

class RecipesListFragment : Fragment() {
    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListRecipesBinding must not to be null")
    private lateinit var category: Category
    private val recipesListViewModel: RecipesListViewModel by viewModels()
    private val recipeListArgs: RecipesListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListRecipesBinding.inflate(layoutInflater)
        category = recipeListArgs.category
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipesListViewModel.loadRecipesList(category)
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

            Glide.with(binding.root)
                .load(it.categoryImageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.ivCategoryHeader)

            // Recycler
            recipesListAdapter.dataSet = it.recipesList
            recipesListAdapter.notifyItemRangeChanged(0, recipesListAdapter.itemCount)

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
        findNavController().navigate(
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(
                recipeId
            )
        )
    }
}