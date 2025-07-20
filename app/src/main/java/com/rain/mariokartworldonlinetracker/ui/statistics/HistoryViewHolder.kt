package com.rain.mariokartworldonlinetracker.ui.statistics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.TrackAndKnockoutHelper
import com.rain.mariokartworldonlinetracker.data.pojo.ResultHistory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryViewHolder(itemView: View) : BaseStatsViewHolder<ResultHistory>(itemView) {

    private val dateTextView: TextView = itemView.findViewById(R.id.date_textview)
    private val drivingFromIconImageView: ImageView = itemView.findViewById(R.id.track_icon)
    private val routeIconImageView: ImageView = itemView.findViewById(R.id.track_icon2)
    private val drivingToIconImageView: ImageView = itemView.findViewById(R.id.track_icon3)
    private val engineClassImageView: ImageView = itemView.findViewById(R.id.engineclass_icon)
    private val positionImageView: ImageView = itemView.findViewById(R.id.position_icon)

    override fun bind(item: ResultHistory) {

        val date = Date(item.creationDate)
        val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN)
        dateTextView.text = format.format(date)

        if (item.drivingFromTrackName != null) {
            drivingFromIconImageView.setImageResource(TrackAndKnockoutHelper.getTrackResId(item.drivingFromTrackName))
            routeIconImageView.setImageResource(R.drawable.route)
        }
        if (item.drivingToTrackName != null) {
            drivingToIconImageView.setImageResource(TrackAndKnockoutHelper.getTrackResId(item.drivingToTrackName))
        } else if (item.knockoutCupName != null) {
            drivingToIconImageView.setImageResource(TrackAndKnockoutHelper.getKnockoutResId(item.knockoutCupName))
        }

        engineClassImageView.setImageResource(TrackAndKnockoutHelper.getEngineClassResId(item.engineClass, item.onlineSessionCategory))
        positionImageView.setImageResource(TrackAndKnockoutHelper.getPositionResId(item.position ?: 999))
    }

    companion object {
        fun create(parent: ViewGroup): HistoryViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_history_detail, parent, false) // Layout für ThreeLapTrack
            return HistoryViewHolder(view)
        }
    }
}

class HistoryDiffCallback : DiffUtil.ItemCallback<ResultHistory>() {
    override fun areItemsTheSame(oldItem: ResultHistory, newItem: ResultHistory): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ResultHistory, newItem: ResultHistory): Boolean {
        return areItemsTheSame(oldItem, newItem) // Data Classes haben eine sinnvolle equals()-Implementierung
    }
}