package com.example.stockportfoliotracker.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.substring
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.stockportfoliotracker.data.Stock
import com.example.stockportfoliotracker.data.StockInfo
import com.example.stockportfoliotracker.databinding.ActivityAddStockBinding
import com.example.stockportfoliotracker.utils.DialogUtils
import com.example.stockportfoliotracker.utils.FirebaseUtils.firebaseDatabase
import com.example.stockportfoliotracker.utils.FirebaseUtils.firebaseUser
import com.example.stockportfoliotracker.viewmodel.FirebaseViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddStock : AppCompatActivity() {

    private lateinit var dialog: AlertDialog
    private var binding: ActivityAddStockBinding? = null
    private val calendar = Calendar.getInstance()
    private var selectedBuyDate = ""
    private var selectedSellDate = ""
    private lateinit var viewModel: FirebaseViewModel
    private var sellPrice = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStockBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        variableInit()
        subscribeUi()
        subscribeClicks()
    }

    private fun variableInit() {
        viewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]
        dialog = DialogUtils.buildLoadingDialog(this@AddStock)
    }

    private fun subscribeUi() {
        val from = intent.getStringExtra("from")
        val stock = intent.getParcelableExtra("stockInfo") as Stock?

        if (from == "edit") {
            binding!!.stockName.setText(stock!!.name)
            binding!!.units.setText(stock.units.toString())
            binding!!.buyPrice.setText(stock.buyPrice.toString())
            binding!!.sellPrice.setText(stock.sellPrice.toString())
            binding!!.buyDatePicker.text = stock.buyDate
            binding!!.sellDatePicker.text = stock.sellDate

        }
    }

    private fun subscribeClicks() {
        selectDate()
        addStock()
    }

    private fun addStock() {
        binding!!.addStockButton.setOnClickListener {
            if (validateInput()) {
                if(binding!!.sellPrice.text.toString().isNotEmpty()) {
                    sellPrice = binding!!.sellPrice.text.toString().toDouble()
                }
                dialog.show()
                val stock = Stock(
                    "",
                    binding!!.stockName.text.toString(),
                    selectedBuyDate,
                    selectedSellDate,
                    binding!!.units.text.toString().toInt(),
                    binding!!.buyPrice.text.toString().toDouble(),
                    sellPrice

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
            .child(stock.name!!)
        val uniqueId = databaseReference.push().key.toString()

        stock.id = uniqueId

        stockRef.child("stockInfo").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val stockInfo = snapshot.getValue(StockInfo::class.java)

                    stockRef.child("list").child(uniqueId).setValue(stock)
                        .addOnSuccessListener {

                            viewModel.addYearWiseData(stock, stock.sellDate!!.substring(6, 10))

                            val investedAmount = String.format(
                                "%.2f",
                                (stockInfo!!.investedAmount + stock.buyPrice * stock.units)
                            ).toDouble()
                            val profit = String.format(
                                "%.2f",
                                (stockInfo.profitAmount + (stock.sellPrice - stock.buyPrice) * stock.units)
                            ).toDouble()
                            val profitGain =
                                String.format("%.2f", (profit / investedAmount) * 100).toDouble()

                            val units = stockInfo.units + stock.units

                            val temp = StockInfo(
                                stock.name, stock.buyPrice * stock.units, (stock.sellPrice - stock.buyPrice) * stock.units, (profit / investedAmount) * 100, stockInfo.units + stock.units
                            )


                            stockRef.child("stockInfo").setValue(
                                StockInfo(
                                    stock.name,
                                    investedAmount, profit, profitGain, units
                                )
                            ).addOnCompleteListener {
                                handleStockAddSuccess(temp)
                            }
                        }
                        .addOnFailureListener {
                            handleStockAddFailure()
                        }

                } else {
                    stockRef.child("list").child(uniqueId).setValue(stock)
                        .addOnSuccessListener {

                            viewModel.addYearWiseData(stock, stock.sellDate!!.substring(6, 10))

                            val investedAmount =
                                String.format("%.2f", stock.buyPrice * stock.units).toDouble()
                            val profit = String.format(
                                "%.2f",
                                (stock.sellPrice - stock.buyPrice) * stock.units
                            ).toDouble()
                            val change =
                                String.format("%.2f", (profit / investedAmount) * 100).toDouble()
                            val units = stock.units

                            val temp = StockInfo(
                                stock.name, investedAmount, profit, change, units
                            )
                            stockRef.child("stockInfo").setValue(
                                temp
                            ).addOnCompleteListener {
                                handleStockAddSuccess(temp)
                            }
                        }
                        .addOnFailureListener {
                            handleStockAddFailure()
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }

    private fun handleStockAddSuccess(stockInfo: StockInfo) {


        viewModel.addUpdateOverviewData(stockInfo)
        viewModel.addYear(selectedBuyDate.substring(6, 10))

        dialog.dismiss()
        Toast.makeText(this@AddStock, "Stock added successfully", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@AddStock, MainActivity::class.java))
        finish()
    }

    private fun handleStockAddFailure() {
        dialog.dismiss()
        startActivity(Intent(this@AddStock, MainActivity::class.java))
        Toast.makeText(this@AddStock, "Failed to add stock", Toast.LENGTH_SHORT).show()
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
                    selectedBuyDate = formattedDate
                } else {
                    selectedSellDate = formattedDate
                }

                view.text = formattedDate

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the DatePicker dialog
        datePickerDialog.show()
    }

    private fun validateInput(): Boolean {
        return binding!!.stockName.text.toString().isNotEmpty()
                && binding!!.units.text.toString().isNotEmpty()
                && binding!!.buyPrice.text.toString().isNotEmpty()
    }
}
