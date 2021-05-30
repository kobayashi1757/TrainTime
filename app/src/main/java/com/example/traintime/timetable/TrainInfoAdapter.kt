package com.example.traintime.timetable

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.traintime.databinding.ListItemTrainInfoBinding
import com.example.traintime.domain.TrainInfo

class TrainInfoAdapter :
        ListAdapter<TrainInfo, TrainInfoAdapter.ViewHolder>(TrainInfoCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trainInfo = getItem(position)
        holder.bind(trainInfo)
    }

    class ViewHolder private constructor(private val binding: ListItemTrainInfoBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(trainInfo: TrainInfo) {
            binding.trainInfo = trainInfo
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemTrainInfoBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TrainInfoCallback : DiffUtil.ItemCallback<TrainInfo>() {

    override fun areItemsTheSame(oldItem: TrainInfo, newItem: TrainInfo): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: TrainInfo, newItem: TrainInfo): Boolean {
        return oldItem == newItem
    }
}