package com.example.stockportfoliotracker.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.stockportfoliotracker.adapter.StockDetailAdapter
import com.example.stockportfoliotracker.data.Stock
import com.example.stockportfoliotracker.data.StockInfo
import com.example.stockportfoliotracker.databinding.ActivityStockDetailsBinding
import com.example.stockportfoliotracker.utils.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class StockDetailsActivity : AppCompatActivity() {

    var binding: ActivityStockDetailsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockDetailsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val stockInfo = intent.getParcelableExtra<StockInfo>("stockInfo")
        binding?.apply {
            stockName.title = stockInfo?.stockName
            investedText.text = "₹${stockInfo?.investedAmount}"
            profitText.text = "₹${stockInfo?.profitAmount}"
            unitsText.text = stockInfo?.units.toString()
            changeText.text = "${stockInfo?.profitGain}%"

            binding!!.stockName.setNavigationOnClickListener {
                onBackPressed()
            }


            val databaseReference = FirebaseUtils.firebaseDatabase.reference
            val stockRef = databaseReference.child(FirebaseUtils.firebaseUser!!.uid).child("stocks")
                .child(stockInfo?.stockName.toString() ).child("list")

            stockRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val stockList = mutableListOf<Stock>()
                    for (stockSnapshot in snapshot.children) {

                        val stock = stockSnapshot.getValue(Stock::class.java)
                        stockList.add(stock!!)
                    }
                    binding?.stockDetailsRc?.adapter = StockDetailAdapter(stockList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("StockDetailsActivity", "Error fetching data", error.toException())
                }
            })
        }
    }
}