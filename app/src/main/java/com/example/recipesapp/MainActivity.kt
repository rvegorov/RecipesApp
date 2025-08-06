package com.example.recipesapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.recipesapp.databinding.ActivityMainBinding
import com.example.recipesapp.model.Category
import kotlinx.serialization.json.Json
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiThread = Thread {
            val url = URL(API_URL + "category")
            val connection = url.openConnection() as HttpsURLConnection
            connection.connect()
            val body = connection.inputStream.bufferedReader().readText()
            Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
            Log.i("!!!", "response code: ${connection.responseCode}")
            Log.i("!!!", "response message: ${connection.responseMessage}")
            Log.i("!!!", "Body: $body")

            val categories: List<Category> = Json.decodeFromString(body)
            Log.i("!!!", "Получено категорий: ${categories.size}")
        }
        apiThread.start()
        Log.i("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnCategories.setOnClickListener {
            findNavController(R.id.mainContainer).navigate(R.id.categoriesListFragment)
        }

        binding.btnFavourites.setOnClickListener {
            findNavController(R.id.mainContainer).navigate(R.id.favoritesFragment)
        }
    }
}