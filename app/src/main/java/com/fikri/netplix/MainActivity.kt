package com.fikri.netplix

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import com.fikri.netplix.core.data.source.Resource
import com.fikri.netplix.core.domain.model.Movie
import com.fikri.netplix.core.ui.adapter.FixedLatestMovieListAdapter
import com.fikri.netplix.databinding.ActivityMainBinding
import com.fikri.netplix.view_model.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupData()
        setupAction()
    }

    private fun setupData() {
        binding.header.tvTitle.text = "Netplix"

        binding.latestMovieList.apply {
            tvListTitle.text = "Latest"
            btnMoreMovie.visibility = View.GONE
            rvHorizontalMovieList.setHasFixedSize(true)
            rvHorizontalMovieList.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        viewModel.apply {
            listLatestMovie.observe(this@MainActivity) { data ->
                when (data) {
                    is Resource.Success -> {
                        binding.apply {
                            smLatestMovie.stopShimmer()
                            smLatestMovie.visibility = View.GONE
                            latestMovieList.root.visibility = View.VISIBLE
                            setLatestMovieList(data.data as ArrayList<Movie>)
                        }
                    }
                    else -> {
                        Toast.makeText(this@MainActivity, "Gagal", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.svMainHome.scrollTo(0, viewModel.scrollviewPositionY)
    }

    private fun setupAction() {
        binding.header.btnRightHeader.setOnClickListener {
            val moveToSearch = Intent(this@MainActivity, SearchMovieActivity::class.java)
            startActivity(
                moveToSearch, ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@MainActivity, Pair(binding.header.ivSearchIcon, "search_icon")
                ).toBundle()
            )
        }
    }

    private fun setLatestMovieList(movieList: ArrayList<Movie>) {
        val fixedLatestMovieListAdapter = FixedLatestMovieListAdapter(movieList)
        binding.latestMovieList.rvHorizontalMovieList.adapter = fixedLatestMovieListAdapter

        fixedLatestMovieListAdapter.setOnItemClickCallback(object :
            FixedLatestMovieListAdapter.OnItemClickCallback {
            override fun onClickedItem(data: Movie) {
                Toast.makeText(this@MainActivity, data.title, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding.smFeaturedMovie.startShimmer()
        binding.smLatestMovie.startShimmer()
        binding.smGenreDiscoverMovie.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        viewModel.scrollviewPositionY = binding.svMainHome.scrollY
    }

    override fun onStop() {
        super.onStop()
        binding.smFeaturedMovie.stopShimmer()
        binding.smLatestMovie.stopShimmer()
        binding.smGenreDiscoverMovie.stopShimmer()
    }
}