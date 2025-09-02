package com.example.recipesapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.databinding.FragmentListCategoriesBinding
import com.example.recipesapp.model.Category
import com.example.recipesapp.ui.categories.CategoriesListViewModel.UiMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListCategoriesBinding must not to be null")


    private val categoriesListViewModel: CategoriesListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        categoriesListViewModel.loadCategoriesList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun initRecycler() {
        val categoriesListAdapter = CategoriesListAdapter(null)
        binding.rvCategories.adapter = categoriesListAdapter

        val categoriesListObserver = Observer<CategoriesListViewModel.CategoriesListState> {

            categoriesListAdapter.dataSet = it.categoriesList
            categoriesListAdapter.notifyItemRangeChanged(0, categoriesListAdapter.itemCount)

            categoriesListAdapter.setOnItemClickListener(object :
                CategoriesListAdapter.OnItemClickListener {
                override fun onItemClick(category: Category) {
                    openRecipesByCategory(category)
                }
            })
        }
        categoriesListViewModel.state.observe(viewLifecycleOwner, categoriesListObserver)

        val uiMessageObserver = Observer<UiMessage> {
            if (it.message != null) {
                Toast.makeText(
                    binding.root.context,
                    it.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        categoriesListViewModel.uiMessage.observe(viewLifecycleOwner, uiMessageObserver)
    }

    fun openRecipesByCategory(category: Category?) {
        if (category == null) {
            throw IllegalArgumentException("Category not found!")
        }

        findNavController().navigate(
            CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                category
            )
        )
    }
}

