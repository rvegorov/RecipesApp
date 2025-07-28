package com.example.recipesapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.ARG_CATEGORY_ID
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListCategoriesBinding must not to be null")
    private val categoriesListViewModel: CategoriesListViewModel by viewModels()

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
            categoriesListAdapter.setOnItemClickListener(object :
                CategoriesListAdapter.OnItemClickListener {
                override fun onItemClick(categoryId: Int) {
                    openRecipesByCategoryId(categoryId)
                }
            })
        }
        categoriesListViewModel.state.observe(viewLifecycleOwner, categoriesListObserver)
    }

    fun openRecipesByCategoryId(categoryId: Int) {
        val bundle = Bundle().apply {
            putInt(ARG_CATEGORY_ID, categoryId)
        }
        findNavController().navigate(R.id.recipesListFragment, bundle)
    }
}