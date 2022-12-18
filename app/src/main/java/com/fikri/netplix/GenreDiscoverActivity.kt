package com.fikri.netplix

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.fikri.netplix.core.data.source.remote.network.Token
import com.fikri.netplix.core.domain.model.Genre
import com.fikri.netplix.core.domain.model.Movie
import com.fikri.netplix.core.ui.adapter.EndlessMovieListAdapter
import com.fikri.netplix.core.ui.adapter.LoadingStateAdapter
import com.fikri.netplix.core.ui.modal.DetailMovieModal
import com.fikri.netplix.core.ui.modal.LoadingModal
import com.fikri.netplix.core.utils.CombinedLoadState.decideOnState
import com.fikri.netplix.databinding.ActivityGenreDiscoverBinding
import com.fikri.netplix.view_model.GenreDiscoverViewModel
import org.koin.android.ext.android.inject

class GenreDiscoverActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SELECTED_GENRE = "extra_selected_genre"
    }

    private lateinit var binding: ActivityGenreDiscoverBinding
    private val viewModel: GenreDiscoverViewModel by inject()
    private var adapter = EndlessMovieListAdapter(this)
    private val detailMovieModal = DetailMovieModal(this)
    private val loadingModal = LoadingModal(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenreDiscoverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupData()
        setupAction()
    }

    private fun setupData() {
//        binding.smEndlessGenreDiscoverMovie.stopShimmer()
//        binding.smEndlessGenreDiscoverMovie.visibility = View.GONE
//        binding.rvMovie.visibility = View.VISIBLE

        binding.header.btnLeftHeader.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_arrow_back
            )
        )
        binding.header.btnRightHeader.visibility = View.GONE

        if (intent.getParcelableExtra<Genre>(EXTRA_SELECTED_GENRE) != null) {
            val receivedGenre = intent.getParcelableExtra<Genre>(EXTRA_SELECTED_GENRE) as Genre
            viewModel.selectedGenre = receivedGenre
            binding.header.tvTitle.text = "Discover by ${receivedGenre.name}"
        }

        viewModel.isShowingLoadingModal.observe(this@GenreDiscoverActivity) {
            if (it) {
                loadingModal.showLoadingModal(message = "Loading")
            } else {
                loadingModal.dismiss()
            }
        }

        viewModel.apply {
            isShowingDetailMovie.observe(this@GenreDiscoverActivity) {
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
                movieVideo = null
            }
        }

        getEndlessMovieByGenre()
    }

    private fun setupAction() {
        binding.header.btnLeftHeader.setOnClickListener {
            onBackPressed()
        }

        binding.srlEndlessMovieList.apply {
            setColorSchemeColors(
                ContextCompat.getColor(
                    this@GenreDiscoverActivity,
                    R.color.secondary_color
                )
            )
            setOnRefreshListener {
                isRefreshing = false
                adapter.submitData(lifecycle, PagingData.empty())
                getEndlessMovieByGenre()
            }
        }

        adapter.addLoadStateListener { combinedLoadStates ->
            combinedLoadStates.decideOnState(
                adapter.itemCount,
                showLoading = { isLoading ->
                    if (isLoading) {
                        binding.apply {
                            smEndlessGenreDiscoverMovie.startShimmer()
                            smEndlessGenreDiscoverMovie.visibility = View.VISIBLE
                            tvEndlessMovieListMessage.visibility = View.GONE
                            rvMovie.visibility = View.GONE
                        }
                    } else {
                        binding.apply {
                            smEndlessGenreDiscoverMovie.stopShimmer()
                            smEndlessGenreDiscoverMovie.visibility = View.GONE
                            tvEndlessMovieListMessage.visibility = View.GONE
                            rvMovie.visibility = View.VISIBLE
                        }
                    }
                },
                showEmptyState = { isEmptyInAdapter ->
                    if (isEmptyInAdapter) {
                        binding.apply {
                            smEndlessGenreDiscoverMovie.stopShimmer()
                            smEndlessGenreDiscoverMovie.visibility = View.GONE
                            tvEndlessMovieListMessage.visibility = View.VISIBLE
                            tvEndlessMovieListMessage.text = "No data available"
                            rvMovie.visibility = View.GONE
                        }
                    }
                },
                showError = {
                    binding.apply {
                        smEndlessGenreDiscoverMovie.stopShimmer()
                        smEndlessGenreDiscoverMovie.visibility = View.GONE
                        tvEndlessMovieListMessage.visibility = View.VISIBLE
                        tvEndlessMovieListMessage.text = "Failed to contact the server"
                        rvMovie.visibility = View.GONE
                    }
                },
            )
        }
    }

    private fun getEndlessMovieByGenre() {
        viewModel.apply {
            binding.apply {
                rvMovie.layoutManager = LinearLayoutManager(this@GenreDiscoverActivity)
                rvMovie.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter(this@GenreDiscoverActivity) {
                        adapter.retry()
                    }
                )
                getEndlessMovieByGenre(Token.TMDB_TOKEN_V3).observe(this@GenreDiscoverActivity) { data ->
                    adapter.submitData(lifecycle, data)
                }

                adapter.setOnItemClickCallback(object :
                    EndlessMovieListAdapter.OnItemClickCallback {
                    override fun onClickedItem(data: Movie) {
                        viewModel.getDetailMovie(data.id ?: 0)
                    }
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detailMovieModal.dismiss()
        loadingModal.dismiss()
    }
}