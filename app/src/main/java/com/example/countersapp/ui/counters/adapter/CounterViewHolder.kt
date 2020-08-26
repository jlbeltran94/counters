package com.example.countersapp.ui.counters.adapter

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.countersapp.R
import com.example.countersapp.databinding.ItemCounterBinding
import com.example.countersapp.ui.models.Counter
import java.lang.IllegalStateException


class CounterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemCounterBinding.bind(itemView)
    private lateinit var counter: Counter
    fun bind(counter: Counter) {
        this.counter = counter
        binding.counterTitle.text = counter.title
        binding.counterCount.text = counter.count.toString()
        bindDecButton(counter)
    }

    fun registerIncreaseListener(listener: ItemActionsListener?) {
        binding.incBtn.setOnClickListener {
            if (this::counter.isInitialized) {
                listener?.onIncCounterAction(counter)
            } else {
                throw IllegalStateException("You have to bind a counter")
            }
        }
    }

    fun registerDecreaseListener(listener: ItemActionsListener?) {
        binding.decBtn.setOnClickListener {
            if (this::counter.isInitialized) {
                listener?.onDecCounterAction(counter)
            } else {
                throw IllegalStateException("You have to bind a counter")
            }
        }
    }

    fun setLongClickAction(action: ((Counter) -> Unit)?) {
        binding.root.setOnLongClickListener {
            action?.invoke(counter)
            true
        }
    }

    private fun bindDecButton(counter: Counter) {
        with(binding.decBtn) {
            isEnabled = counter.count > 0
            val color = if (isEnabled) R.color.colorPrimary else R.color.colorDisabled
            ImageViewCompat.setImageTintList(
                this, ColorStateList.valueOf(
                    ContextCompat.getColor(context, color)
                )
            )
        }
    }
}