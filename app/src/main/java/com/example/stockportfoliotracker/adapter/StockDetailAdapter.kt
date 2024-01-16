package com.example.stockportfoliotracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stockportfoliotracker.R
import com.example.stockportfoliotracker.data.Stock
import com.example.stockportfoliotracker.data.StockInfo

class StockDetailAdapter(private val stockList: MutableList<Stock>) :
    RecyclerView.Adapter<StockDetailAdapter.StockDetailViewHolder>() {

    class StockDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val invested: TextView = itemView.findViewById(R.id.investedText)
        val profit: TextView = itemView.findViewById(R.id.profitText)
        val units: TextView = itemView.findViewById(R.id.unitsText)
        val change: TextView = itemView.findViewById(R.id.changeText)
        val buyDate: TextView = itemView.findViewById(R.id.buyDateText)
        val sellDate: TextView = itemView.findViewById(R.id.sellDateText)
        val sellPrice: TextView = itemView.findViewById(R.id.sellPriceText)
        val buyPrice: TextView = itemView.findViewById(R.id.buyPriceTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockDetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.stock_details_itemview, parent, false)
        return StockDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockDetailViewHolder, position: Int) {
        val stock = stockList[position]

        val buyPrice = stock.buyPrice
        val sellPrice = stock.sellPrice
        val units = stock.units

        val investedAmount = buyPrice * units
        val profitAmount = (sellPrice - buyPrice) * units
        val profitGain = (profitAmount / investedAmount) * 100

        // Set data to the views
        holder.invested.text = "₹$investedAmount"
        holder.profit.text = "₹$profitAmount"
        holder.units.text = stock.units.toString()
        holder.change.text = "${profitGain}%"
        holder.buyDate.text = stock.buyDate
        holder.sellDate.text = stock.sellDate
        holder.sellPrice.text = "₹${stock.sellPrice}"
        holder.buyPrice.text = "₹${stock.buyPrice}"
    }

    override fun getItemCount(): Int {
        return stockList.size
    }
}
