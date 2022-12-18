package com.fikri.netplix.core.ui.modal

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fikri.netplix.R
import com.fikri.netplix.core.data.source.remote.network.Server
import com.fikri.netplix.core.domain.model.MovieDetail
import com.fikri.netplix.core.domain.model.MovieVideo
import com.fikri.netplix.core.ui.adapter.CompanyOfMovieListAdapter
import com.fikri.netplix.core.ui.adapter.GenreOfMovieListAdapter
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class DetailMovieModal(private val context: Context) :
    MyModal() {

    fun showDetailMovieModal(
        movieDetail: MovieDetail? = null,
        movieVideo: MovieVideo? = null,
        onCloseButtonPressed: (() -> Unit)? = null,
        onWebsiteButtonPressed: ((link: Uri) -> Unit)? = null
    ) {
        modal = Dialog(context, android.R.style.Theme_Material_Light_NoActionBar)
        modal?.setContentView(R.layout.movie_detail_modal)

        val ytTrailer = modal?.findViewById<YouTubePlayerView>(R.id.yt_trailer)
        val ibCloseModal = modal?.findViewById<ImageButton>(R.id.ib_close_modal)
        val ivBackdrop = modal?.findViewById<ImageView>(R.id.iv_backdrop)
        val ivPoster = modal?.findViewById<ImageView>(R.id.iv_poster)
        val tvTitle = modal?.findViewById<TextView>(R.id.tv_title)
        val tvDuration = modal?.findViewById<TextView>(R.id.tv_duration)
        val tvRating = modal?.findViewById<TextView>(R.id.tv_rating)
        val tvTagline = modal?.findViewById<TextView>(R.id.tv_tagline)
        val btnWebsite = modal?.findViewById<Button>(R.id.btn_website)
        val rvGenre = modal?.findViewById<RecyclerView>(R.id.rv_genre)
        val rvCompanies = modal?.findViewById<RecyclerView>(R.id.rv_companies)
        val tvOverview = modal?.findViewById<TextView>(R.id.tv_overview)

        rvGenre?.setHasFixedSize(true)
        rvGenre?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvCompanies?.setHasFixedSize(true)
        rvCompanies?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        ibCloseModal?.setOnClickListener {
            ytTrailer?.release()
            onCloseButtonPressed?.invoke()
        }

        movieVideo?.let {
            ytTrailer?.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    youTubePlayer.cueVideo(it.key ?: "Unknown Key", 0f)
                }
            })
        }

        movieDetail?.let {
            ivBackdrop?.let { view ->
                Glide.with(context)
                    .load(Server.TMDB_IMAGE_BASE_URL + it.backdropPath)
                    .error(R.drawable.default_image)
                    .into(view)
            }

            ivPoster?.let { view ->
                Glide.with(context)
                    .load(Server.TMDB_IMAGE_BASE_URL + it.posterPath)
                    .error(R.drawable.default_image)
                    .into(view)
            }

            tvTitle?.text = it.title
            tvDuration?.text = "${it.runtime.toString()} Minutes"
            tvRating?.text = "${it.voteAverage} ( ${it.voteCount}  of vote )"
            tvTagline?.text = it.tagline
            tvOverview?.text = it.overview

            val genreOfMovieListAdapter = GenreOfMovieListAdapter(it.genres)
            rvGenre?.adapter = genreOfMovieListAdapter
            val companyOfMovieListAdapter = CompanyOfMovieListAdapter(it.productionCompanies)
            rvCompanies?.adapter = companyOfMovieListAdapter

            btnWebsite?.setOnClickListener { _ ->
                onWebsiteButtonPressed?.invoke(Uri.parse(it.homepage))
            }
        }

        showModal()
    }
}