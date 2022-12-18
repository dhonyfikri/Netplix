package com.fikri.netplix.core.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fikri.netplix.R
import com.fikri.netplix.databinding.CarouselIndicatorItemBinding

class CarouselIndicatorAdapter(
    private val context: Context,
    private val listState: ArrayList<Boolean>
) :
    RecyclerView.Adapter<CarouselIndicatorAdapter.ListViewHolder>() {

    class ListViewHolder(var binding: CarouselIndicatorItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            CarouselIndicatorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val isActive = listState[position]
        holder.binding.apply {
            vwObjectItem.background = ContextCompat.getDrawable(
                context,
                if (isActive) R.drawable.bg_active_carousel_indicator_item else R.drawable.bg_carousel_indicator_item
            )
        }
    }

    override fun getItemCount() = listState.size
}