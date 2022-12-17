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
import com.fikri.netplix.core.domain.model.Genre
import com.fikri.netplix.core.domain.model.Movie
import com.fikri.netplix.core.ui.adapter.FixedLatestMovieListAdapter
import com.fikri.netplix.core.ui.adapter.GenreDiscoverAdapter
import com.fikri.netplix.core.ui.modal.DetailMovieModal
import com.fikri.netplix.core.ui.modal.LoadingModal
import com.fikri.netplix.databinding.ActivityMainBinding
import com.fikri.netplix.view_model.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    private val detailMovieModal = DetailMovieModal(this)
    private val loadingModal = LoadingModal(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupData()
        setupAction()
    }

    private fun setupData() {
        binding.header.tvTitle.text = "Netplix"
        binding.header.ivSearchIcon.visibility = View.VISIBLE

        binding.latestMovieList.apply {
            tvListTitle.text = "Latest"
            btnMoreMovie.visibility = View.GONE
            rvHorizontalMovieList.setHasFixedSize(true)
            rvHorizontalMovieList.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.apply {
            rvGenreDiscoverMovieListParent.setHasFixedSize(true)
            rvGenreDiscoverMovieListParent.layoutManager = LinearLayoutManager(this@MainActivity)
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
                        Toast.makeText(this@MainActivity, "Latest Movie Gagal", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            listGenre.observe(this@MainActivity) {
                it.getContentIfNotHandled()?.let { data ->
                    when (data) {
                        is Resource.Success -> {
                            getGenresMembers(data.data as ArrayList<Genre>)
                        }
                        else -> {
                            Toast.makeText(this@MainActivity, "Genre Gagal", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }

            listGenreWithMember.observe(this@MainActivity) { data ->
                binding.apply {
                    smGenreDiscoverMovie.stopShimmer()
                    smGenreDiscoverMovie.visibility = View.GONE
                    rlGenreDiscoverMovieListParentWrapper.visibility = View.VISIBLE
                }
                setGenreList(data)
            }

            isShowingLoadingModal.observe(this@MainActivity) {
                if (it) {
                    loadingModal.showLoadingModal(message = "Loading")
                } else {
                    loadingModal.dismiss()
                }
            }

            isShowingDetailMovie.observe(this@MainActivity) {
                if (it) {
                    detailMovieModal.showDetailMovieModal(
                        movieDetail = movieDetail,
                        movieVideo = movieVideo,
                        onCloseButtonPressed = {
                            dismissDetailMovie()
                        },
                        onWebsiteButtonPressed = { uri ->
                            startActivity(
                                Intent(Intent.ACTION_VIEW, uri)
                            )
                        })
                } else {
                    detailMovieModal.dismiss()
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
                viewModel.getDetailMoview(data.id ?: 0)
            }
        })
    }

    private fun setGenreList(genreList: ArrayList<Genre>) {
        val genreDiscoverAdapter = GenreDiscoverAdapter(this@MainActivity, genreList)
        binding.rvGenreDiscoverMovieListParent.adapter = genreDiscoverAdapter

        genreDiscoverAdapter.setOnSeeMoreClickCallback(object :
            GenreDiscoverAdapter.OnSeeMoreClickCallback {
            override fun onClickedSeeMore(data: Genre) {
                val moveToGenreDiscover =
                    Intent(this@MainActivity, GenreDiscoverActivity::class.java)
                moveToGenreDiscover.putExtra(GenreDiscoverActivity.EXTRA_SELECTED_GENRE, data)
                startActivity(moveToGenreDiscover)
            }
        })

        genreDiscoverAdapter.setOnMovieMemberClickCallback(object :
            GenreDiscoverAdapter.OnMovieMemberClickCallback {
            override fun onClickedMovieMember(data: Movie) {
                viewModel.getDetailMoview(data.id ?: 0)
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
        binding.smFeaturedMovie.stopShimmer()
        binding.smLatestMovie.stopShimmer()
        binding.smGenreDiscoverMovie.stopShimmer()
        viewModel.scrollviewPositionY = binding.svMainHome.scrollY
    }

    override fun onDestroy() {
        super.onDestroy()
        detailMovieModal.dismiss()
        loadingModal.dismiss()
    }
}