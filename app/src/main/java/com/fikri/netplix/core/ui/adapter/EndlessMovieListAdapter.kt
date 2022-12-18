package com.fikri.netplix.core.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fikri.netplix.R
import com.fikri.netplix.core.data.source.remote.network.Server
import com.fikri.netplix.core.domain.model.Movie
import com.fikri.netplix.core.helper.DimensManipulation.dpToPx
import com.fikri.netplix.databinding.MovieDetailedItemBinding

class EndlessMovieListAdapter(private val context: Context) :
    PagingDataAdapter<Movie, EndlessMovieListAdapter.ListViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(
                oldItem: Movie,
                newItem: Movie
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Movie,
                newItem: Movie
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(var binding: MovieDetailedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            context: Context,
            pos: Int,
            movie: Movie,
            onClicked: ((value: Movie) -> Unit)? = null
        ) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(Server.TMDB_IMAGE_BASE_URL + movie.posterPath)
                    .error(R.drawable.default_image)
                    .into(binding.ivPoster)
                tvMovieTitle.text = movie.title
                tvOverview.text = movie.overview
                tvReleaseDate.text = "Release on ${movie.releaseDate}"
                tvRating.text = "${movie.voteAverage} ( ${movie.voteCount} of vote )"
                cvMovieItem.setOnClickListener {
                    onClicked?.invoke(movie)
                }

                val params = cvMovieItem.layoutParams as RecyclerView.LayoutParams
                params.setMargins(
                    params.leftMargin,
                    if (pos == 0) {
                        dpToPx(context, 12f).toInt()
                    } else {
                        dpToPx(context, 4f).toInt()
                    },
                    params.rightMargin,
                    params.bottomMargin
                )
                cvMovieItem.layoutParams = params
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            MovieDetailedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) {
            holder.bind(context, position, movie, onClicked = { value ->
                onItemClickCallback.onClickedItem(value)
            })
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onClickedItem(data: Movie)
    }
}