package com.example.peliculasapi
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val apiKey = "262f617af17a0422c74a9e4c5a9ef9c3" // Reemplaza con tu clave de API de MovieDB
    private var currentPage = 1
    private var isLoading = false
    private val movies = mutableListOf<Movie>()
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        movieAdapter = MovieAdapter(movies) { movie -> navigateToDetailActivity(movie) }
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = movieAdapter

        loadMovies()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                    currentPage++
                    loadMovies()
                }
            }
        })
    }

    private fun loadMovies() {
        isLoading = true

        val movieApiService = RetrofitClient.createService(MovieApiService::class.java)
        val call = movieApiService.getPopularMovies(apiKey, currentPage)

        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val newMovies = response.body()?.results ?: emptyList()
                    movies.addAll(newMovies)
                    movieAdapter.notifyDataSetChanged()
                }
                isLoading = false
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                isLoading = false
                // Manejar el fallo, por ejemplo, mostrar un mensaje de error
            }
        })
    }

    private fun navigateToDetailActivity(movie: Movie) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra("movie", movie)
        startActivity(intent)
    }
}

