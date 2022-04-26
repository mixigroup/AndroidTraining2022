package jp.co.mixi.androidtraining2022

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.mixi.androidtraining2022.databinding.LapTimeItemBinding

class LapTimeAdapter : ListAdapter<LapTime, LapTimeAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LapTimeItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lapTime = getItem(position)

        holder.binding.lapNumber.text = lapTime.number.toString()
        holder.binding.lapTime.text = LapTime.getTimeText(lapTime.time)
    }

    class ViewHolder(
        val binding: LapTimeItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback : DiffUtil.ItemCallback<LapTime>() {
        override fun areItemsTheSame(oldItem: LapTime, newItem: LapTime): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(oldItem: LapTime, newItem: LapTime): Boolean {
            return oldItem == newItem
        }
    }
}