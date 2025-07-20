package com.rain.mariokartworldonlinetracker.ui.statistics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.TrackAndKnockoutHelper
import com.rain.mariokartworldonlinetracker.data.pojo.ThreeLapTrackDetailedData

interface DetailedData {}

class StatisticsListAdapter<T : DetailedData, VH : BaseStatsViewHolder<T>>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val viewHolderCreator: (parent: ViewGroup, viewType: Int) -> VH
) : ListAdapter<T, VH>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return viewHolderCreator(parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

abstract class BaseStatsViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
}