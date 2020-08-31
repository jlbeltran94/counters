package com.example.countersapp.ui.examples.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.countersapp.R
import com.example.countersapp.databinding.ItemChipBinding
import com.example.countersapp.ui.examples.listeners.ChipClickListener
import com.example.countersapp.util.inflate

class ChipAdapter : RecyclerView.Adapter<ChipViewHolder>() {
    var data: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var listener: ChipClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        return ChipViewHolder(parent.inflate(R.layout.item_chip))
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        holder.bind(data[position], listener)
    }

    override fun getItemCount(): Int = data.size
}

class ChipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemChipBinding.bind(itemView)
    fun bind(chip: String, listener: ChipClickListener?) {
        binding.root.text = chip
        binding.root.setOnClickListener {
            listener?.onChipSelected(chip)
        }
    }
}