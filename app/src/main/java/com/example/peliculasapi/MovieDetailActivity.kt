package com.example.peliculasapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var releaseYearTextView: TextView
    private lateinit var overviewTextView: TextView
    private lateinit var posterImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        titleTextView = findViewById(R.id.titleTextView)
        releaseYearTextView = findViewById(R.id.releaseYearTextView)
        overviewTextView = findViewById(R.id.overviewTextView)
        posterImageView = findViewById(R.id.posterImageView)

        val movie = intent.getParcelableExtra<Movie>("movie")
        if (movie != null) {
            titleTextView.text = movie.title
            releaseYearTextView.text = movie.releaseYear
            overviewTextView.text = movie.overview

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                .into(posterImageView)
        }
    }
}
