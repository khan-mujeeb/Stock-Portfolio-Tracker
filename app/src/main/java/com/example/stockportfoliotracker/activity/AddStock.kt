package com.example.stockportfoliotracker.activity

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.stockportfoliotracker.R
import com.example.stockportfoliotracker.data.Stock
import com.example.stockportfoliotracker.databinding.ActivityAddStockBinding
import com.example.stockportfoliotracker.utils.FirebaseUtils.firebaseDatabase
import com.example.stockportfoliotracker.utils.FirebaseUtils.firebaseUser
import java.util.Locale

class AddStock : AppCompatActivity() {

    var binding: ActivityAddStockBinding? = null
    private val calendar = Calendar.getInstance()
    private var selectedDate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStockBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        subscribeUi()
        subscribeClicks()

    }

    private fun subscribeUi() {
        setInitialDate()
    }

    private fun subscribeClicks() {
        selectDate()
        addStock()
    }

    private fun addStock() {
        binding!!.addStockButton.setOnClickListener {
            if (vaildateInput()) {


                val stock = Stock(
                    binding!!.stockName.text.toString(),
                    selectedDate,
                    binding!!.units.text.toString(),
                    binding!!.buyPrice.text.toString(),
                    binding!!.sellPrice.text.toString()
                )

                addStockToFb(stock)

            } else {
                Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addStockToFb(stock: Stock) {
        firebaseDatabase.reference.child(firebaseUser!!.uid).child("stocks")
            .child(stock.name).setValue(stock)
            .addOnSuccessListener {
                Toast.makeText(this, "Stock added successfully", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add stock", Toast.LENGTH_SHORT).show()
            }
    }

    private fun selectDate() {
        binding!!.btnDatePicker.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                binding!!.btnDatePicker.text = formattedDate.toString()
                this.selectedDate = formattedDate.toString()
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

    }

    private fun setInitialDate() {
        val initialDate = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(initialDate.time)
        binding!!.btnDatePicker.text = formattedDate
        this.selectedDate = formattedDate
    }
}