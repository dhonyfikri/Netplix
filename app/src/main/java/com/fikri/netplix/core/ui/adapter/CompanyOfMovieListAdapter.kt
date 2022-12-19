package com.fikri.netplix.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fikri.netplix.core.data.source.remote.network.Server
import com.fikri.netplix.core.domain.model.ProductionCompanies
import com.fikri.netplix.core.utils.GlideUtils
import com.fikri.netplix.databinding.CompanyItemBinding

class CompanyOfMovieListAdapter(private val listCompanyOfMovie: ArrayList<ProductionCompanies>) :
    RecyclerView.Adapter<CompanyOfMovieListAdapter.ListViewHolder>() {

    class ListViewHolder(var binding: CompanyItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            CompanyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val company = listCompanyOfMovie[position]
        holder.binding.apply {
            Glide.with(holder.itemView.context)
                .load(Server.TMDB_IMAGE_BASE_URL + company.logoPath)
                .thumbnail(GlideUtils.thumbnailQuality)
                .apply(GlideUtils.requestOptions)
                .into(ivLogo)
            tvCompanyName.text = company.name
        }
    }

    override fun getItemCount() = listCompanyOfMovie.size
}