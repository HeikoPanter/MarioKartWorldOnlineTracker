package com.rain.mariokartworldonlinetracker.ui.statistics.race

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.TrackAndKnockoutHelper
import com.rain.mariokartworldonlinetracker.data.pojo.ThreeLapTrackDetailedData
import com.rain.mariokartworldonlinetracker.ui.statistics.BaseStatsViewHolder

class TrackViewHolder(itemView: View) : BaseStatsViewHolder<ThreeLapTrackDetailedData>(itemView) {
    private val trackIconImageView: ImageView = itemView.findViewById(R.id.track_icon3)
    private val raceAmountTextView: TextView = itemView.findViewById(R.id.total_amount_textview)
    private val averagePositionImageView: ImageView = itemView.findViewById(R.id.position_icon)

    override fun bind(item: ThreeLapTrackDetailedData) {
        trackIconImageView.setImageResource(TrackAndKnockoutHelper.getTrackResId(item.drivingToTrackName))
        raceAmountTextView.text = item.amountOfRaces.toString()
        averagePositionImageView.setImageResource(TrackAndKnockoutHelper.getPositionResId(item.averagePosition.toShort()))
    }

    companion object {
        fun create(parent: ViewGroup): TrackViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_track_detail, parent, false) // Layout für ThreeLapTrack
            return TrackViewHolder(view)
        }
    }
}

class TrackDiffCallback : DiffUtil.ItemCallback<ThreeLapTrackDetailedData>() {
    override fun areItemsTheSame(oldItem: ThreeLapTrackDetailedData, newItem: ThreeLapTrackDetailedData): Boolean {
        return oldItem.drivingToTrackName == newItem.drivingToTrackName
    }

    override fun areContentsTheSame(oldItem: ThreeLapTrackDetailedData, newItem: ThreeLapTrackDetailedData): Boolean {
        return oldItem == newItem // Data Classes haben eine sinnvolle equals()-Implementierung
    }
}