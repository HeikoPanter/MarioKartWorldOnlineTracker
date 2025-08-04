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
import com.rain.mariokartworldonlinetracker.data.pojo.HistoryListItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(private val onListUpdated: () -> Unit) : ListAdapter<HistoryListItem, RecyclerView.ViewHolder>(HistoryDiffCallback()) {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    class SessionHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sessionIdTextView: TextView = itemView.findViewById(R.id.textViewSessionIdHeader) // ID anpassen
        private val sessionDateTextView: TextView = itemView.findViewById(R.id.textViewSessionDateHeader) // ID anpassen

        fun bind(header: HistoryListItem.SessionHeaderItem) {
            sessionIdTextView.text = header.sessionId.toString()
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            sessionDateTextView.text = dateFormat.format(Date(header.sessionCreationDate))
        }
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val dateTextView: TextView = itemView.findViewById(R.id.date_textview)
        private val drivingFromIconImageView: ImageView = itemView.findViewById(R.id.track_icon)
        private val routeIconImageView: ImageView = itemView.findViewById(R.id.track_icon2)
        private val drivingToIconImageView: ImageView = itemView.findViewById(R.id.track_icon3)
        private val engineClassImageView: ImageView = itemView.findViewById(R.id.engineclass_icon)
        private val positionImageView: ImageView = itemView.findViewById(R.id.position_icon)

        fun bind(item: HistoryListItem.ResultHistoryItem) {

            val date = Date(item.resultHistory.creationDate)
            val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN)
            dateTextView.text = format.format(date)

            if (item.resultHistory.drivingFromTrackName != null) {
                drivingFromIconImageView.setImageResource(
                    TrackAndKnockoutHelper.getTrackResId(
                        item.resultHistory.drivingFromTrackName))
                routeIconImageView.setImageResource(
                    R.drawable.route)
            } else {
                drivingFromIconImageView.setImageResource(0)
                routeIconImageView.setImageResource(0)
            }
            if (item.resultHistory.drivingToTrackName != null) {
                drivingToIconImageView.setImageResource(
                    TrackAndKnockoutHelper.getTrackResId(
                        item.resultHistory.drivingToTrackName))
            } else if (item.resultHistory.knockoutCupName != null) {
                drivingToIconImageView.setImageResource(
                    TrackAndKnockoutHelper.getKnockoutResId(
                        item.resultHistory.knockoutCupName))
            } else {
                drivingToIconImageView.setImageResource(0)
            }

            engineClassImageView.setImageResource(
                TrackAndKnockoutHelper.getEngineClassResId(
                    item.resultHistory.engineClass,
                    item.resultHistory.onlineSessionCategory))
            positionImageView.setImageResource(
                TrackAndKnockoutHelper.getPositionResId(
                    item.resultHistory.position ?: 999))
        }
    }

    class HistoryDiffCallback : DiffUtil.ItemCallback<HistoryListItem>() {
        override fun areItemsTheSame(oldItem: HistoryListItem, newItem: HistoryListItem): Boolean {
            return when {
                oldItem is HistoryListItem.SessionHeaderItem && newItem is HistoryListItem.SessionHeaderItem ->
                    oldItem.id == newItem.id // Vergleiche die generierte ID
                oldItem is HistoryListItem.ResultHistoryItem && newItem is HistoryListItem.ResultHistoryItem ->
                    oldItem.resultHistory.id == newItem.resultHistory.id // Nutze die ID des ResultHistory-Objekts
                else -> false // Unterschiedliche Item-Typen sind niemals dieselben Items
            }
        }

        override fun areContentsTheSame(oldItem: HistoryListItem, newItem: HistoryListItem): Boolean {
            return oldItem == newItem // Data classes vergleichen ihre Inhalte korrekt
        }
    }

    override fun onCurrentListChanged(
        previousList: MutableList<HistoryListItem>,
        currentList: MutableList<HistoryListItem>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        // Dieser Callback wird aufgerufen, NACHDEM die neue Liste im Adapter ist.
        // Hier ist ein guter Ort, um das Scrollen auszulösen.

        if (currentList.isNotEmpty() && previousList != currentList) {
            // Rufe den Callback auf, um das Fragment zu informieren, dass es scrollen soll.
            onListUpdated()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HistoryListItem.SessionHeaderItem -> TYPE_HEADER
            is HistoryListItem.ResultHistoryItem -> TYPE_ITEM
            // null sollte hier nicht auftreten, wenn die Liste korrekt befüllt ist
            null -> throw IllegalStateException("Item at position $position is null")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_history_session_header, parent, false) // Dein Header-Layout
                SessionHeaderViewHolder(view)
            }
            TYPE_ITEM -> {
                // Verwende hier dein bestehendes item_history_detail.xml Layout
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_history_detail, parent, false)
                HistoryViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is HistoryListItem.SessionHeaderItem -> (holder as SessionHeaderViewHolder).bind(item)
            is HistoryListItem.ResultHistoryItem -> (holder as HistoryViewHolder).bind(item)
            null -> { /* Optional: Handle null case, though ListAdapter should prevent this if used correctly */ }
        }
    }
}