package com.example.recipesapp.ui.recipes.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.RecipesApplication
import com.example.recipesapp.databinding.FragmentFavouritesBinding
import com.example.recipesapp.ui.recipes.favourites.FavouritesViewModel.FavouritesState
import com.example.recipesapp.ui.recipes.recipeList.RecipesListAdapter

class FavouritesFragment : Fragment() {
    private var _binding: FragmentFavouritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListCategoriesBinding must not to be null")

    private lateinit var favouritesViewModel: FavouritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        favouritesViewModel = appContainer.favouritesViewModelFactory.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        favouritesViewModel.loadRecipesList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun initRecycler() {
        var recipesListAdapter = RecipesListAdapter(null)
        binding.rvRecipes.adapter = recipesListAdapter

        val favouritesObserver = Observer<FavouritesState> {
            val favouritesSet = it.recipesList?.toSet()
            favouritesSet?.run {
                if (favouritesSet.isNotEmpty()) {
                    recipesListAdapter.dataSet = it.recipesList
                    recipesListAdapter.notifyItemRangeChanged(0, recipesListAdapter.itemCount)

                    recipesListAdapter.setOnItemClickListener(object :
                        RecipesListAdapter.OnItemClickListener {
                        override fun onItemClick(recipeId: Int) {
                            openRecipeByRecipeId(recipeId)
                        }
                    })
                } else {
                    binding.rvRecipes.visibility = View.GONE
                    binding.favouritesPlaceholder.visibility = View.VISIBLE
                }
            }
        }
        favouritesViewModel.state.observe(viewLifecycleOwner, favouritesObserver)

        val uiMessageObserver = Observer<FavouritesViewModel.UiMessage> {
            if (it.message != null) {
                Toast.makeText(
                    binding.root.context,
                    it.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        favouritesViewModel.uiMessage.observe(viewLifecycleOwner, uiMessageObserver)
    }

    fun openRecipeByRecipeId(recipeId: Int) {
        findNavController().navigate(
            FavouritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(
                recipeId
            )
        )
    }
}