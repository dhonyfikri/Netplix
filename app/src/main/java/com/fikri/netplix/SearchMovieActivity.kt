package com.fikri.netplix

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.fikri.netplix.core.domain.model.Movie
import com.fikri.netplix.core.ui.adapter.FixedSearchMovieListAdapter
import com.fikri.netplix.core.ui.modal.DetailMovieModal
import com.fikri.netplix.core.ui.modal.LoadingModal
import com.fikri.netplix.core.ui.modal.RefreshModal
import com.fikri.netplix.core.utils.KeyboardUtils.hideKeyboard
import com.fikri.netplix.databinding.ActivitySearchMovieBinding
import com.fikri.netplix.view_model.SearchMovieViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchMovieBinding
    private val viewModel: SearchMovieViewModel by viewModel()

    private val detailMovieModal = DetailMovieModal(this)
    private val loadingModal = LoadingModal(this)
    private val refreshModal = RefreshModal(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupData()
        setupAction()
    }

    private fun setupData() {
        binding.apply {
            rvMovieSearchList.setHasFixedSize(true)
            rvMovieSearchList.layoutManager =
                GridLayoutManager(this@SearchMovieActivity, 2, GridLayoutManager.VERTICAL, false)
        }

        viewModel.apply {
            isShowingShimmer.observe(this@SearchMovieActivity) {
                if (it) {
                    binding.apply {
                        smMovieSearch.startShimmer()
                        smMovieSearch.visibility = View.VISIBLE
                        rvMovieSearchList.visibility = View.GONE
                        tvMovieSearchListMessage.visibility = View.GONE
                    }
                } else {
                    binding.apply {
                        smMovieSearch.stopShimmer()
                        smMovieSearch.visibility = View.GONE
                        rvMovieSearchList.visibility = View.VISIBLE
                        tvMovieSearchListMessage.visibility = View.GONE
                    }
                }
            }

            listMovieSearchResult.observe(this@SearchMovieActivity) {
                setSearchMovieList(it)
                if (it.isNotEmpty()) {
                    binding.rvMovieSearchList.visibility = View.VISIBLE
                    binding.tvMovieSearchListMessage.visibility = View.GONE
                } else {
                    binding.rvMovieSearchList.visibility = View.GONE
                    binding.tvMovieSearchListMessage.visibility = View.VISIBLE
                    binding.tvMovieSearchListMessage.text = getString(R.string.no_data_available)
                }
            }

            isShowingLoadingModal.observe(this@SearchMovieActivity) {
                if (it) {
                    loadingModal.showLoadingModal(message = getString(R.string.loading))
                } else {
                    loadingModal.dismiss()
                }
            }

            isShowingRefreshModal.observe(this@SearchMovieActivity) {
                if (it) {
                    refreshModal.showRefreshModal(
                        type = RefreshModal.TYPE_FAILED,
                        message = getString(R.string.failed_to_contact_the_server),
                        onRefreshClicked = {
                            dismissRefreshModal()
                            getDetailMovieTryAgain()
                        },
                        onCloseClicked = { dismissRefreshModal() }
                    )
                } else {
                    refreshModal.dismiss()
                }
            }

            isShowingDetailMovie.observe(this@SearchMovieActivity) {
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
    }

    private fun setupAction() {
        binding.apply {
            header.btnLeftHeader.setOnClickListener {
                onBackPressed()
            }

            header.etSearch.setOnEditorActionListener { textView, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (textView.text.trim().isNotEmpty()) {
                        viewModel.searchMovie(textView.text.toString())
                    }
                    hideKeyboard()
                    textView.clearFocus()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            header.ibClearSearctText.setOnClickListener {
                header.etSearch.setText("")
            }
        }
    }

    private fun setSearchMovieList(movieList: ArrayList<Movie>) {
        val fixedSearchMovieListAdapter = FixedSearchMovieListAdapter(movieList)
        binding.rvMovieSearchList.adapter = fixedSearchMovieListAdapter

        fixedSearchMovieListAdapter.setOnItemClickCallback(object :
            FixedSearchMovieListAdapter.OnItemClickCallback {
            override fun onClickedItem(data: Movie) {
                viewModel.getDetailMovie(data.id ?: 0)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        detailMovieModal.dismiss()
        loadingModal.dismiss()
        refreshModal.dismiss()
    }
}