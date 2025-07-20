package com.rain.mariokartworldonlinetracker.ui.statistics.knockout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.TrackAndKnockoutHelper
import com.rain.mariokartworldonlinetracker.data.pojo.RallyDetailedData
import com.rain.mariokartworldonlinetracker.data.pojo.ThreeLapTrackDetailedData
import com.rain.mariokartworldonlinetracker.ui.statistics.BaseStatsViewHolder

class RallyViewHolder(itemView: View) : BaseStatsViewHolder<RallyDetailedData>(itemView) {
    private val rallyIconImageView: ImageView = itemView.findViewById(R.id.track_icon3)
    private val raceAmountTextView: TextView = itemView.findViewById(R.id.total_amount_textview)
    private val averagePositionImageView: ImageView = itemView.findViewById(R.id.position_icon)

    override fun bind(item: RallyDetailedData) {
        rallyIconImageView.setImageResource(TrackAndKnockoutHelper.getKnockoutResId(item.knockoutCupName))
        raceAmountTextView.text = item.amountOfRaces.toString()
        averagePositionImageView.setImageResource(TrackAndKnockoutHelper.getPositionResId(item.averagePosition.toShort()))
    }

    companion object {
        fun create(parent: ViewGroup): RallyViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_track_detail, parent, false) // Layout für ThreeLapTrack
            return RallyViewHolder(view)
        }
    }
}

class RallyDiffCallback : DiffUtil.ItemCallback<RallyDetailedData>() {
    override fun areItemsTheSame(oldItem: RallyDetailedData, newItem: RallyDetailedData): Boolean {
        return oldItem.knockoutCupName == newItem.knockoutCupName &&
                oldItem.amountOfRaces == newItem.amountOfRaces &&
                oldItem.averagePosition == newItem.averagePosition
    }

    override fun areContentsTheSame(oldItem: RallyDetailedData, newItem: RallyDetailedData): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }
}