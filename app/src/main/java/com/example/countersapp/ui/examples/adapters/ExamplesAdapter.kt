package com.example.countersapp.ui.examples.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.countersapp.R
import com.example.countersapp.databinding.ItemExampleBinding
import com.example.countersapp.ui.examples.listeners.ChipClickListener
import com.example.countersapp.ui.models.Example
import com.example.countersapp.util.inflate

class ExamplesAdapter : RecyclerView.Adapter<ExampleViewHolder>() {

    var data: List<Example> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var listener: ChipClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        return ExampleViewHolder(parent.inflate(R.layout.item_example))
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        holder.bind(data[position], listener)
    }

    override fun getItemCount(): Int = data.size
}

class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemExampleBinding.bind(itemView)
    private val chipAdapter by lazy { ChipAdapter() }
    fun bind(example: Example, chipClickListener: ChipClickListener?) {
        binding.exampleCattegory.text = example.category
        chipAdapter.listener = chipClickListener
        binding.chipsRecycler.adapter = chipAdapter
        chipAdapter.data = example.options
    }
}
