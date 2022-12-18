package com.fikri.netplix.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fikri.netplix.R
import com.fikri.netplix.core.data.source.remote.network.Server
import com.fikri.netplix.core.domain.model.Movie
import com.fikri.netplix.databinding.FeaturedMovieItemBinding

class FeaturedMovieListAdapter(
    private val listMovie: ArrayList<Movie>
) :
    RecyclerView.Adapter<FeaturedMovieListAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(var binding: FeaturedMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            FeaturedMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val movie = listMovie[position]

        Glide.with(holder.itemView.context)
            .load(Server.TMDB_IMAGE_BASE_URL + movie.posterPath)
            .error(R.drawable.default_image)
            .into(holder.binding.ivPoster)
        holder.binding.apply {
            tvMovieTitle.text = movie.title
            tvOverview.text = movie.overview
            tvReleaseDate.text = "Release on ${movie.releaseDate}"
            tvRating.text = "${movie.voteAverage} ( ${movie.voteCount} of vote )"
        }

        holder.binding.cvMovieItem.setOnClickListener {
            onItemClickCallback.onClickedItem(listMovie[position])
        }
    }

    override fun getItemCount() = listMovie.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onClickedItem(data: Movie)
    }
}