package com.example.countersapp.ui.counters.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.countersapp.databinding.ItemCounterSelectableBinding
import com.example.countersapp.ui.models.Counter
import com.example.countersapp.util.visible

class SelectableCounterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemCounterSelectableBinding.bind(itemView)
    private lateinit var counter: Counter
    fun bind(counter: Counter) {
        this.counter = counter
        binding.counterTitle.text = counter.title
    }

    fun handleSelectedState(selected: Boolean) {
        binding.group.visible(selected)
    }

    fun setClickAction(action: ((Counter) -> Unit)?) {
        binding.root.setOnClickListener {
            action?.invoke(counter)
        }
    }
}