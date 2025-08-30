package com.example.recipesapp.ui.recipes.recipeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipesapp.databinding.FragmentListRecipesBinding
import com.example.recipesapp.model.Category
import com.example.recipesapp.ui.recipes.recipeList.RecipesListViewModel.RecipesListState
import com.example.recipesapp.R
import com.example.recipesapp.RecipesApplication
import com.example.recipesapp.ui.recipes.recipeList.RecipesListViewModel.UiMessage

class RecipesListFragment : Fragment() {
    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListRecipesBinding must not to be null")
    private lateinit var category: Category

    private lateinit var recipesListViewModel: RecipesListViewModel
    private val recipeListArgs: RecipesListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        recipesListViewModel = appContainer.recipesListViewModelFactory.create()
    }

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
        binding.tvCategoryTitle.text = category.title
        binding.rvRecipes.adapter = recipesListAdapter

        val recipesListStateObserver = Observer<RecipesListState> {
            // Category UI
            if (it.category?.title != null) {
                binding.tvCategoryTitle.text = it.category?.title
            }

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

        val uiMessageObserver = Observer<UiMessage> {
            if (it.message != null) {
                Toast.makeText(
                    context,
                    it.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        recipesListViewModel.uiMessage.observe(
            viewLifecycleOwner,
            uiMessageObserver
        )
    }

    fun openRecipeByRecipeId(recipeId: Int) {
        findNavController().navigate(
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(
                recipeId
            )
        )
    }
}