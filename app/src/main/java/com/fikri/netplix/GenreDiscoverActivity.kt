package com.fikri.netplix

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.fikri.netplix.core.data.source.remote.network.Token
import com.fikri.netplix.core.domain.model.Genre
import com.fikri.netplix.core.domain.model.Movie
import com.fikri.netplix.core.ui.adapter.EndlessMovieListAdapter
import com.fikri.netplix.core.ui.adapter.LoadingStateAdapter
import com.fikri.netplix.databinding.ActivityGenreDiscoverBinding
import com.fikri.netplix.view_model.GenreDiscoverViewModel
import org.koin.android.ext.android.inject

class GenreDiscoverActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SELECTED_GENRE = "extra_selected_genre"
    }

    private lateinit var binding: ActivityGenreDiscoverBinding
    private val viewModel: GenreDiscoverViewModel by inject()
    private var adapter = EndlessMovieListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenreDiscoverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.smEndlessGenreDiscoverMovie.stopShimmer()
        binding.smEndlessGenreDiscoverMovie.visibility = View.GONE
        binding.rvMovie.visibility = View.VISIBLE

        binding.header.btnLeftHeader.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_arrow_back
            )
        )
        binding.header.btnRightHeader.visibility = View.GONE

        binding.header.btnLeftHeader.setOnClickListener {
            onBackPressed()
        }

        if (intent.getParcelableExtra<Genre>(EXTRA_SELECTED_GENRE) != null) {
            val receivedGenre = intent.getParcelableExtra<Genre>(EXTRA_SELECTED_GENRE) as Genre
            viewModel.selectedGenre = receivedGenre
            binding.header.tvTitle.text = "Discover by ${receivedGenre.name}"
        }

        getEndlessMovieByGenre()
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
                getMovieByGenre(Token.TMDB_TOKEN_V3).observe(this@GenreDiscoverActivity) { data ->
                    adapter.submitData(lifecycle, data)
                }

                adapter.setOnItemClickCallback(object :
                    EndlessMovieListAdapter.OnItemClickCallback {
                    override fun onClickedItem(data: Movie, posterView: View) {
                        Toast.makeText(this@GenreDiscoverActivity, data.title, Toast.LENGTH_SHORT)
                            .show()
//                        val moveToMovieDetail =
//                            Intent(this@GenreDiscoverActivity, MovieDetailActivity::class.java)
//                        moveToMovieDetail.putExtra(MovieDetailActivity.EXTRA_MOVIE, data)
//                        moveToMovieDetail.putExtra(MovieDetailActivity.EXTRA_GENRE, genresOfMovie)
//                        startActivity(
//                            moveToMovieDetail, ActivityOptionsCompat.makeSceneTransitionAnimation(
//                                this@GenreDiscoverActivity, Pair(posterView, "poster_image_view")
//                            ).toBundle()
//                        )
                    }
                })
            }
        }
    }
}