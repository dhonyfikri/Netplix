package com.fikri.netplix

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fikri.netplix.databinding.ActivitySearchMovieBinding

class SearchMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.btnLeftHeader.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.smMovieSearch.startShimmer()
    }

    override fun onStop() {
        super.onStop()
        binding.smMovieSearch.stopShimmer()
    }
}