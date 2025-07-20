package com.rain.mariokartworldonlinetracker.ui.statistics.race

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.TrackAndKnockoutHelper
import com.rain.mariokartworldonlinetracker.data.pojo.RouteDetailedData
import com.rain.mariokartworldonlinetracker.data.pojo.ThreeLapTrackDetailedData
import com.rain.mariokartworldonlinetracker.ui.statistics.BaseStatsViewHolder

class RouteViewHolder(itemView: View) : BaseStatsViewHolder<RouteDetailedData>(itemView) {
    private val drivingFromIconImageView: ImageView = itemView.findViewById(R.id.track_icon)
    private val routeIconImageView: ImageView = itemView.findViewById(R.id.track_icon2)
    private val drivingToIconImageView: ImageView = itemView.findViewById(R.id.track_icon3)
    private val raceAmountTextView: TextView = itemView.findViewById(R.id.total_amount_textview)
    private val averagePositionImageView: ImageView = itemView.findViewById(R.id.position_icon)

    override fun bind(item: RouteDetailedData) {
        drivingFromIconImageView.setImageResource(TrackAndKnockoutHelper.getTrackResId(item.drivingFromTrackName))
        routeIconImageView.setImageResource(R.drawable.route)
        drivingToIconImageView.setImageResource(TrackAndKnockoutHelper.getTrackResId(item.drivingToTrackName))
        raceAmountTextView.text = item.amountOfRaces.toString()
        averagePositionImageView.setImageResource(TrackAndKnockoutHelper.getPositionResId(item.averagePosition.toShort()))
    }

    companion object {
        fun create(parent: ViewGroup): RouteViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_track_detail, parent, false) // Layout für ThreeLapTrack
            return RouteViewHolder(view)
        }
    }
}

class RouteDiffCallback : DiffUtil.ItemCallback<RouteDetailedData>() {
    override fun areItemsTheSame(oldItem: RouteDetailedData, newItem: RouteDetailedData): Boolean {
        return oldItem.drivingToTrackName == newItem.drivingToTrackName && oldItem.drivingFromTrackName == newItem.drivingFromTrackName
    }

    override fun areContentsTheSame(oldItem: RouteDetailedData, newItem: RouteDetailedData): Boolean {
        return oldItem == newItem // Data Classes haben eine sinnvolle equals()-Implementierung
    }
}