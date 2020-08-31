package com.example.countersapp.ui.counters.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.countersapp.R
import com.example.countersapp.ui.counters.listeners.ItemActionsListener
import com.example.countersapp.ui.models.Counter
import com.example.countersapp.util.inflate

class CountersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isSelectionEnabled = false
        set(value) {
            if (field != value) {
                field = value
                if (value.not()) {
                    selectedCounters.clear()
                }
                listener?.onSelectionModeChanged(value)
                notifyDataSetChanged()
            }
        }

    var counters: List<Counter> = listOf()
        set(value) {
            field = value
            updateSelectedCounters(value)
            verifyCountersSelected()
            notifyDataSetChanged()
        }

    var listener: ItemActionsListener? = null
    private var selectedCounters = hashMapOf<Counter, Boolean>()

    override fun getItemViewType(position: Int): Int {
        return if (isSelectionEnabled) R.layout.item_counter_selectable else R.layout.item_counter
    }

    override fun getItemCount(): Int = counters.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = parent.inflate(viewType)
        return getViewHolder(itemView)
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

    fun getSelectedCounters(): List<Counter> {
        return selectedCounters.mapNotNull { entry -> entry.key.takeIf { entry.value } }
    }

    private fun getViewHolder(itemView: View): RecyclerView.ViewHolder {
        return if (isSelectionEnabled) {
            SelectableCounterViewHolder(itemView)
        } else {
            CounterViewHolder(itemView)
        }
    }
    private fun getSelectedItemsCount(): Int = selectedCounters.values.count { it }

    private fun bindSelectableCounterViewHolder(
        currentCounter: Counter,
        holder: SelectableCounterViewHolder,
        position: Int
    ) {
        val currentState = selectedCounters[currentCounter] ?: false
        holder.bind(currentCounter)
        holder.handleSelectedState(currentState)
        holder.setClickAction { counter ->
            val newState = currentState.not()
            selectedCounters[counter] = newState
            if (selectedCounters.values.any { it }.not()) {
                isSelectionEnabled = false
            } else {
                notifyItemChanged(position)
                listener?.onSelectionChanges(getSelectedItemsCount())
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
            val currentState = selectedCounters[counter] ?: false
            val newState = currentState.not()
            selectedCounters[counter] = newState
            isSelectionEnabled = true
            listener?.onSelectionChanges(getSelectedItemsCount())
        }
    }

    private fun verifyCountersSelected() {
        val selectedCount = getSelectedItemsCount()
        val areCountersSelected = selectedCount > 0
        if (areCountersSelected) {
            listener?.onSelectionChanges(selectedCount)
        } else {
            isSelectionEnabled = false
        }
    }

    private fun updateSelectedCounters(newCounters: List<Counter>) {
        val currentSelectedCounters = HashMap(selectedCounters)
        selectedCounters.apply {
            clear()
            newCounters.forEach {
                this[it] = currentSelectedCounters[it] ?: false
            }
        }
    }
}
