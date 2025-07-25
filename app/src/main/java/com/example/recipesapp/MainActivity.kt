package com.example.recipesapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.example.recipesapp.databinding.ActivityMainBinding
import com.example.recipesapp.ui.categories.CategoriesListFragment
import com.example.recipesapp.ui.recipes.favourites.FavoritesFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val categoriesListFragment = CategoriesListFragment()
        val favoritesFragment = FavoritesFragment()
        val mainContainerViewId = R.id.mainContainer

        supportFragmentManager.commit { add(mainContainerViewId, categoriesListFragment) }

        binding.btnCategories.setOnClickListener {
            supportFragmentManager.commit {
                replace(mainContainerViewId, categoriesListFragment)
                setReorderingAllowed(true)
            }
        }

        binding.btnFavourites.setOnClickListener {
            supportFragmentManager.commit {
                replace(mainContainerViewId, favoritesFragment)
                setReorderingAllowed(true)
            }
        }
    }
}