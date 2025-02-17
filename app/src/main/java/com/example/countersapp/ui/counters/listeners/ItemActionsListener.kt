package com.example.countersapp.ui.counters.listeners

import com.example.countersapp.ui.models.Counter

interface ItemActionsListener {

    fun onIncCounterAction(counter: Counter)
    fun onDecCounterAction(counter: Counter)
    fun onSelectionModeChanged(isSelectedEnabled: Boolean)
    fun onSelectionChanges(selectedItems: Int)
}