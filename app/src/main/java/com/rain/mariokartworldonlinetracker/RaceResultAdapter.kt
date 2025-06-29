package com.rain.mariokartworldonlinetracker.ui.statistics // Passen Sie den Paketnamen an

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rain.mariokartworldonlinetracker.RaceResult // Ihre Entitätsklasse
import com.rain.mariokartworldonlinetracker.databinding.ListItemRaceResultBinding // ViewBinding für das Item-Layout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RaceResultAdapter(private val onItemClicked: (RaceResult) -> Unit) :
    ListAdapter<RaceResult, RaceResultAdapter.RaceResultViewHolder>(RaceResultDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaceResultViewHolder {
        val binding = ListItemRaceResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RaceResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RaceResultViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            onItemClicked(currentItem)
        }
    }

    class RaceResultViewHolder(private val binding: ListItemRaceResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        fun bind(raceResult: RaceResult) {
            binding.textViewDrivingFromTrackName.text = "From: ${raceResult.drivingFromTrackName ?: "N/A"}"
            binding.textViewDrivingToTrackName.text = "To: ${raceResult.drivingToTrackName ?: "N/A"}"
            binding.textViewRacePosition.text = "Position: ${raceResult.position ?: "N/A"}"
            binding.textViewRaceDate.text = dateFormat.format(Date(raceResult.date))
            // Binden Sie hier weitere Felder Ihrer RaceResult-Entität
        }
    }

    class RaceResultDiffCallback : DiffUtil.ItemCallback<RaceResult>() {
        override fun areItemsTheSame(oldItem: RaceResult, newItem: RaceResult): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RaceResult, newItem: RaceResult): Boolean {
            return oldItem == newItem
        }
    }
}