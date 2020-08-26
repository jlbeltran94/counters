package com.example.countersapp.ui.counters.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.countersapp.R
import com.example.countersapp.ui.models.Counter
import com.example.countersapp.util.inflate

class CountersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isSelectionEnabled = false
        set(value) {
            if (field != value) {
                field = value
                listener?.onSelectionModeChanged(value)
            }
        }
    var counters: List<Counter> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var listener: ItemActionsListener? = null
    var selectedCounters = hashMapOf<String, Boolean>()

    override fun getItemViewType(position: Int): Int {
        return if (isSelectionEnabled) R.layout.item_counter_selectable else R.layout.item_counter
    }

    override fun getItemCount(): Int = counters.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = parent.inflate(viewType)
        return if (isSelectionEnabled) {
            SelectableCounterViewHolder(itemView)
        } else {
            CounterViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val counter = counters[position]
        when (holder) {
            is CounterViewHolder -> {
                bindCounterViewHolder(holder, counter)
            }
            is SelectableCounterViewHolder -> {
                bindSelectableCounterViewHolder(counter, holder, position)
            }
        }
    }

    private fun bindSelectableCounterViewHolder(
        currentCounter: Counter,
        holder: SelectableCounterViewHolder,
        position: Int
    ) {
        val currentState = selectedCounters[currentCounter.id] ?: false
        holder.bind(currentCounter)
        holder.handleSelectedState(currentState)
        holder.setClickAction { counter ->
            val newState = currentState.not()
            selectedCounters[counter.id] = newState
            if (selectedCounters.values.any { it }.not()) {
                isSelectionEnabled = false
                notifyDataSetChanged()
            } else {
                notifyItemChanged(position)
            }
        }
    }

    private fun bindCounterViewHolder(
        holder: CounterViewHolder,
        currentCounter: Counter
    ) {
        holder.bind(currentCounter)
        holder.registerDecreaseListener(listener)
        holder.registerIncreaseListener(listener)
        holder.setLongClickAction { counter ->
            val currentState = selectedCounters[counter.id] ?: false
            val newState = currentState.not()
            selectedCounters[counter.id] = newState
            isSelectionEnabled = true
            notifyDataSetChanged()
        }
    }


}
