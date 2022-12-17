package com.fikri.netplix.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fikri.netplix.core.domain.model.Genre
import com.fikri.netplix.databinding.GenreItemOutlinedBinding

class GenreOfMovieListAdapter(private val listGenreOfMovie: ArrayList<Genre>) :
    RecyclerView.Adapter<GenreOfMovieListAdapter.ListViewHolder>() {

    class ListViewHolder(var binding: GenreItemOutlinedBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            GenreItemOutlinedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val genre = listGenreOfMovie[position]
        holder.binding.tvGenreName.text = genre.name
    }

    override fun getItemCount() = listGenreOfMovie.size
}