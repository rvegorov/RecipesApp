package com.example.recipesapp

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipesapp.databinding.FragmentFavouritesBinding
import java.util.HashSet

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavouritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListCategoriesBinding must not to be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecycler()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun initRecycler() {
        val favouritesSet = getFavourites().map {
            it.toInt()
        }.toSet()
        if (favouritesSet.isNotEmpty()) {
            val recipesListAdapter = RecipesListAdapter(STUB.getRecipesByIds(favouritesSet))
            recipesListAdapter.setOnItemClickListener(object :
                RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            }
            )
            val recycler = binding.rvRecipes
            recycler.adapter = recipesListAdapter
        } else {
            binding.rvRecipes.visibility = View.GONE
            binding.favouritesPlaceholder.visibility = View.VISIBLE
        }
    }

    fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        val bundle = Bundle().apply {
            recipe?.let {
                putParcelable(ARG_RECIPE, recipe)
            }
        }

        this.parentFragmentManager.commit {
            replace<RecipeFragment>(containerViewId = R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
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
}