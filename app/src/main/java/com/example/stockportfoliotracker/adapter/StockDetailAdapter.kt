package com.example.stockportfoliotracker.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stockportfoliotracker.R
import com.example.stockportfoliotracker.activity.AddStock
import com.example.stockportfoliotracker.activity.StockDetailsActivity
import com.example.stockportfoliotracker.data.Stock
import com.example.stockportfoliotracker.data.StockInfo
import com.example.stockportfoliotracker.viewmodel.FirebaseViewModel

class StockDetailAdapter(val context: Context, private val stockList: MutableList<Stock>, val viewModel: FirebaseViewModel) :
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
        val deleteBtn: TextView = itemView.findViewById(R.id.deleteBtn)
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
        holder.change.text = String.format("%.2f", profitGain) + "%"
        holder.buyDate.text = stock.buyDate
        holder.sellDate.text = stock.sellDate
        holder.sellPrice.text = "₹${stock.sellPrice}"
        holder.buyPrice.text = "₹${stock.buyPrice}"

        holder.itemView.setOnClickListener{
            val intent = Intent(context, AddStock::class.java)
            intent.putExtra("from", "edit")
            intent.putExtra("stockInfo", stock)
            context.startActivity(intent)
        }


        holder.deleteBtn.setOnClickListener {
            viewModel.removeStock(stock)
        }


    }

    override fun getItemCount(): Int {
        return stockList.size
    }
}
