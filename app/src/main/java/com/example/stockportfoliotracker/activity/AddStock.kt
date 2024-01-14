package com.example.stockportfoliotracker.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.stockportfoliotracker.data.Stock
import com.example.stockportfoliotracker.data.StockInfo
import com.example.stockportfoliotracker.databinding.ActivityAddStockBinding
import com.example.stockportfoliotracker.utils.DialogUtils
import com.example.stockportfoliotracker.utils.FirebaseUtils.firebaseDatabase
import com.example.stockportfoliotracker.utils.FirebaseUtils.firebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class AddStock : AppCompatActivity() {

    private lateinit var dialog: AlertDialog
    var binding: ActivityAddStockBinding? = null
    private val calendar = Calendar.getInstance()
    private var selectedBuyDate = ""
    private var selectedSellDate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStockBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        variableInit()
        subscribeUi()
        subscribeClicks()

    }

    private fun variableInit() {
        dialog = DialogUtils.buildLoadingDialog(this@AddStock)
    }

    private fun subscribeUi() {
//        setInitialDate()
    }

    private fun subscribeClicks() {
        selectDate()
        addStock()
    }

    private fun addStock() {
        binding!!.addStockButton.setOnClickListener {
            if (vaildateInput()) {

                dialog.show()
                val stock = Stock(
                    binding!!.stockName.text.toString(),
                    selectedBuyDate,
                    selectedSellDate,
                    binding!!.units.text.toString().toInt(),
                    binding!!.buyPrice.text.toString().toDouble(),
                    binding!!.sellPrice.text.toString().toDouble()
                )

                addStockToFb(stock)

            } else {
                Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addStockToFb(stock: Stock) {

        val databaseReference = firebaseDatabase.reference
        val stockRef = databaseReference.child(firebaseUser!!.uid).child("stocks")
            .child(stock.name)
        val uniqueId = databaseReference.push().key.toString()

        stockRef.child("stockInfo").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val stockInfo = snapshot.getValue(StockInfo::class.java)

                    stockRef.child("list").child(uniqueId).setValue(stock)
                        .addOnSuccessListener {

                            val investedAmount = stockInfo!!.investedAmount + stock.buyPrice * stock.units
                            val profit = stockInfo.profitAmount + (stock.sellPrice - stock.buyPrice) * stock.units
                            val profitGain = (profit / investedAmount) * 100

                            stockRef.child("stockInfo").setValue(
                                StockInfo(
                                    investedAmount, profit, profitGain
                                )
                            ).addOnCompleteListener {
                                dialog.dismiss()
                                Toast.makeText(
                                    this@AddStock,
                                    "Stock added successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(
                                    Intent(this@AddStock, MainActivity::class.java)
                                )
                                finish()
                            }

                        }
                        .addOnFailureListener {
                            dialog.dismiss()
                            startActivity(
                                Intent(this@AddStock, MainActivity::class.java)
                            )
                            Toast.makeText(this@AddStock, "Failed to add stock", Toast.LENGTH_SHORT)
                                .show()
                        }

                } else {
                    stockRef.child("list").child(uniqueId).setValue(stock)
                        .addOnSuccessListener {
                            val investedAmount = stock.buyPrice * stock.units
                            val profit = (stock.sellPrice - stock.buyPrice)*stock.units
                            val change = (profit / investedAmount) * 100
                            stockRef.child("stockInfo").setValue(
                                StockInfo(
                                    investedAmount, profit, change
                                )
                            ).addOnCompleteListener {
                                dialog.dismiss()
                                Toast.makeText(
                                    this@AddStock,
                                    "Stock added successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(
                                    Intent(this@AddStock, MainActivity::class.java)
                                )
                                finish()
                            }
                        }
                        .addOnFailureListener {
                            dialog.dismiss()
                            startActivity(
                                Intent(this@AddStock, MainActivity::class.java)
                            )
                            Toast.makeText(this@AddStock, "Failed to add stock", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }


    private fun selectDate() {
        binding!!.buyDatePicker.setOnClickListener {
            showDatePicker("buy", binding!!.buyDatePicker)
        }

        binding!!.sellDatePicker.setOnClickListener {
            showDatePicker("sell", binding!!.sellDatePicker)
        }
    }

    private fun showDatePicker(from: String, view: TextView) {
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this, { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                if (from == "buy") {
                    this.selectedBuyDate = formattedDate.toString()
                } else {
                    this.selectedSellDate = formattedDate.toString()
                }

                view.text = formattedDate.toString()

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the DatePicker dialog
        datePickerDialog.show()
    }

    private fun vaildateInput(): Boolean {
        return binding!!.stockName.text.toString().isNotEmpty()
                && binding!!.units.text.toString().isNotEmpty()
                && binding!!.buyPrice.text.toString().isNotEmpty()
                && binding!!.sellPrice.text.toString().isNotEmpty()
                && selectedBuyDate.isNotEmpty() && selectedSellDate.isNotEmpty()
    }

//    private fun setInitialDate() {
//        val initialDate = Calendar.getInstance()
//        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//        val formattedDate = dateFormat.format(initialDate.time)
//        binding!!.btnDatePicker.text = formattedDate
//        this.selectedDate = formattedDate
//    }
}