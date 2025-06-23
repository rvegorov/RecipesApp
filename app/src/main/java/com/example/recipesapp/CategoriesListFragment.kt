package com.example.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipesapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListCategoriesBinding must not to be null")

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun initRecycler() {
        val categoriesListAdapter = CategoriesListAdapter(STUB.getCategories())
        categoriesListAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
        val recycler = binding.rvCategories
        recycler.adapter = categoriesListAdapter
    }

    fun openRecipesByCategoryId(categoryId: Int) {
        val category: Category? =
            STUB.getCategories().firstOrNull { category -> category.id == categoryId }
        val categoryName = category?.title
        val categoryImageUrl = category?.imageUrl
        val bundle = Bundle().apply {
            category?.let {
                putInt(ARG_CATEGORY_ID, categoryId)
                putString(ARG_CATEGORY_NAME, categoryName)
                putString(ARG_CATEGORY_IMAGE_URL, categoryImageUrl)
            }
        }

        this.parentFragmentManager.commit {
            replace<RecipesListFragment>(containerViewId = R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
        }
    }
}