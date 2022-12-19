package com.fikri.netplix.core.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fikri.netplix.R
import com.fikri.netplix.core.domain.model.Genre
import com.fikri.netplix.core.domain.model.Movie
import com.fikri.netplix.databinding.HorizontalMovieListBinding

class GenreDiscoverAdapter(private val context: Context, private val listGenre: ArrayList<Genre>) :
    RecyclerView.Adapter<GenreDiscoverAdapter.ListViewHolder>() {
    private lateinit var onSeeMoreClickCallback: OnSeeMoreClickCallback
    private lateinit var onMovieMemberClickCallback: OnMovieMemberClickCallback

    class ListViewHolder(var binding: HorizontalMovieListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            HorizontalMovieListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val genre = listGenre[position]

        holder.apply {
            binding.apply {
                rvHorizontalMovieList.setHasFixedSize(true)
                rvHorizontalMovieList.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                tvListTitle.text = genre.name

                val fixedGenreDiscoverMovieListAdapter =
                    FixedGenreDiscoverMovieListAdapter(genre.member)
                rvHorizontalMovieList.adapter = fixedGenreDiscoverMovieListAdapter

                if (genre.member.isNotEmpty()) {
                    rvHorizontalMovieList.visibility = View.VISIBLE
                    tvHorizontalMovieListMessage.visibility = View.GONE
                } else {
                    rvHorizontalMovieList.visibility = View.GONE
                    tvHorizontalMovieListMessage.visibility =
                        View.VISIBLE
                    tvHorizontalMovieListMessage.text =
                        context.getString(R.string.no_data_available)
                }

                btnMoreMovie.setOnClickListener {
                    onSeeMoreClickCallback.onClickedSeeMore(listGenre[position])
                }

                fixedGenreDiscoverMovieListAdapter.setOnItemClickCallback(object :
                    FixedGenreDiscoverMovieListAdapter.OnItemClickCallback {
                    override fun onClickedItem(data: Movie) {
                        onMovieMemberClickCallback.onClickedMovieMember(data)
                    }
                })
            }
        }
    }

    override fun getItemCount() = listGenre.size

    fun setOnSeeMoreClickCallback(onItemClickCallback: OnSeeMoreClickCallback) {
        this.onSeeMoreClickCallback = onItemClickCallback
    }

    fun setOnMovieMemberClickCallback(onItemClickCallback: OnMovieMemberClickCallback) {
        this.onMovieMemberClickCallback = onItemClickCallback
    }

    interface OnSeeMoreClickCallback {
        fun onClickedSeeMore(data: Genre)
    }

    interface OnMovieMemberClickCallback {
        fun onClickedMovieMember(data: Movie)
    }
}