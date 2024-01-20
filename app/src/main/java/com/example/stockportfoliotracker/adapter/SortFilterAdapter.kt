package com.example.stockportfoliotracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stockportfoliotracker.R  // Replace with your app's R package
import com.example.stockportfoliotracker.fragments.SortBottomSheetFragment
import com.example.stockportfoliotracker.utils.utils

class SortFilterAdapter(
    private val context: Context,
    private val filterList: List<String>,

) : RecyclerView.Adapter<SortFilterAdapter.SortFilterViewHolder>() {




    class SortFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filterText: TextView = itemView.findViewById(R.id.filterText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortFilterViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sort_filter_item, parent, false)
        return SortFilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: SortFilterViewHolder, position: Int) {
        val filter = filterList[position]

        holder.filterText.text = filter

        holder.itemView.setOnClickListener {
            when (filter) {
                "Stock Name" -> {
                    utils.sortBy.value = "name"
                }
                "Quantity" -> {
                    utils.sortBy.value = "quantity"
                }
                "Profit Amount" -> {
                    utils.sortBy.value = "profitAmount"
                }
                "Invested Amount" -> {
                    utils.sortBy.value = "investedAmount"
                }
                "Profit Percentage" -> {
                    utils.sortBy.value = "profitPercentage"
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return filterList.size
    }
}
