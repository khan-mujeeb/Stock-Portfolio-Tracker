package com.example.stockportfoliotracker.adapter

import android.os.Build.VERSION_CODES.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stockportfoliotracker.R
import com.example.stockportfoliotracker.data.StockItemView

class StockListAdapter(private val stockList: List<StockItemView>) :
    RecyclerView.Adapter<StockListAdapter.StockViewHolder>() {

    class StockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stockName: TextView = itemView.findViewById(R.id.stockName)
        val invested: TextView = itemView.findViewById(R.id.investedText)
        val profit: TextView = itemView.findViewById(R.id.profitText)
        val units: TextView = itemView.findViewById(R.id.unitsText)
        val change: TextView = itemView.findViewById(R.id.changeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.stock_list_itemview, parent, false)
        return StockViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stockItem = stockList[position]

        println("virat " + stockItem)


        holder.invested.text = stockItem.stockInfo.investedAmount.toString()
        holder.profit.text = stockItem.stockInfo.profitAmount.toString()
        holder.stockName.text = stockItem.stockInfo.stockName
        holder.units.text = stockItem.stockInfo.stockName
        holder.change.text = stockItem.stockInfo.profitGain.toString()
    }

    override fun getItemCount(): Int {
        return stockList.size
    }


}