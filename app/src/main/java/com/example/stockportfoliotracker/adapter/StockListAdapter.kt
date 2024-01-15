package com.example.stockportfoliotracker.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.stockportfoliotracker.R
import com.example.stockportfoliotracker.activity.AddStock
import com.example.stockportfoliotracker.activity.StockDetailsActivity
import com.example.stockportfoliotracker.data.StockItemView

class StockListAdapter(val context: Context, private val stockList: List<StockItemView>) :
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


        holder.invested.text = "₹${stockItem.stockInfo.investedAmount}"
        holder.profit.text = "₹${stockItem.stockInfo.profitAmount}"
        holder.stockName.text = stockItem.stockInfo.stockName
        holder.units.text = stockItem.stockInfo.units.toString()
        holder.change.text = "${stockItem.stockInfo.profitGain}%"

        holder.itemView.setOnClickListener {
            val intent = Intent(context, StockDetailsActivity::class.java)
            intent.putExtra("stockInfo", stockItem.stockInfo)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return stockList.size
    }


}