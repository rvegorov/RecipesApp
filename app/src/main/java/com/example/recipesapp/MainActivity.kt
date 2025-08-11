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
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val threadPool = Executors.newFixedThreadPool(10)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiThread = Thread {
            lateinit var body: String
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(logging).build()
            val request: Request = Request.Builder().url(API_URL + "category").build()
            client.newCall(request).execute().use { response ->
                body = response.body.string()
                Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
            }

            val categories: List<Category> = Json.decodeFromString(body)
            val categoryIdList = categories.map { it.id }
            Log.i("!!!", "Получено категорий: ${categories.size}")

            categoryIdList.forEach { id ->
                threadPool.execute {
                    val request: Request =
                        Request.Builder().url(API_URL + "category/$id/recipes").build()
                    client.newCall(request).execute().use {
                        Log.i("!!!", "Поток: ${Thread.currentThread().name}, категория: $id")
                    }
                }
            }
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