package jp.co.mixi.androidtraining2022

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.mixi.androidtraining2022.databinding.LapTimeItemBinding

class LapTimeAdapter : ListAdapter<LapTime, LapTimeAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = LapTimeItemBinding.inflate(inflator, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lapTime = getItem(position)

        holder.binding.lapNumber.text = lapTime.number.toString()

        val time = lapTime.time

        val minute = time / (1000 * 60)
        val second = time / 1000 % 60
        val milliSecond = time % 1000

        holder.binding.lapTime.text = String.format(
            "%02d:%02d.%02d",
            minute,
            second,
            milliSecond / 10 // 上位2桁を表示するため
        )
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