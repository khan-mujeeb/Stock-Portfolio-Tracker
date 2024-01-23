package com.example.stockportfoliotracker.repository

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stockportfoliotracker.data.Stock
import com.example.stockportfoliotracker.data.StockInfo
import com.example.stockportfoliotracker.data.StockItemView
import com.example.stockportfoliotracker.data.User
import com.example.stockportfoliotracker.data.YearlyProfit
import com.example.stockportfoliotracker.utils.FirebaseUtils
import com.example.stockportfoliotracker.utils.FirebaseUtils.stockInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class FirebaseRepository {

    val databaseReference = FirebaseUtils.firebaseDatabase.reference
    val userRef = databaseReference.child(FirebaseUtils.firebaseUser!!.uid)
    val stockRef = databaseReference.child(FirebaseUtils.firebaseUser!!.uid).child("stocks")


    /*
            ADD FUNCTIONS
     */

    //    add user
    fun addUser(
        user: User,
        phonenumber: String,
        currentContext: Context,
        GoToNextActivity: AppCompatActivity
    ) {


        databaseReference.child("users").child(phonenumber).setValue(user).addOnSuccessListener {
            startActivity(
                currentContext,
                Intent(currentContext, GoToNextActivity::class.java),
                null
            )
        }

            .addOnFailureListener {
                Toast.makeText(
                    currentContext,
                    "Failed to add user to the database",
                    Toast.LENGTH_LONG
                ).show()
            }


    }

    // add update Overview Data
    fun addUpdateOverviewData(
        overviewData: StockInfo
    ) {

        databaseReference.child(FirebaseUtils.firebaseUser!!.uid)
            .child("overviewData").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val stockInfo = snapshot.getValue(StockInfo::class.java)

                            val investedAmount = stockInfo!!.investedAmount + overviewData.investedAmount
                            val profitAmount = stockInfo.profitAmount + overviewData.profitAmount
                            val profitGain =  String.format("%.2f", (profitAmount / investedAmount) * 100).toDouble()

                            val updatedData = StockInfo(
                                stockInfo.stockName,
                                investedAmount,
                                profitAmount,
                                profitGain

                            )
                            println("investedAmount: ${stockInfo!!.investedAmount} + ${overviewData.investedAmount} = $investedAmount")
                            addStockInfo(updatedData)

                    } else {
                        addStockInfo(overviewData)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error if needed

                }
            })

    }

    fun addYearWiseData(stock: Stock, year: String) {
        databaseReference.child(FirebaseUtils.firebaseUser!!.uid)
            .child("yearlyData").child(year).child(stock.id!!).setValue(stock).addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }

    fun addStockInfo(stockInfo: StockInfo) {

        databaseReference.child(FirebaseUtils.firebaseUser!!.uid)
            .child("overviewData").setValue(stockInfo).addOnSuccessListener {

            }

            .addOnFailureListener {

            }
    }


    fun addYear(year: String) {
        databaseReference.child(FirebaseUtils.firebaseUser!!.uid)
            .child("year").child(year).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        databaseReference.child(FirebaseUtils.firebaseUser!!.uid)
                            .child("year").setValue(year).addOnSuccessListener {

                            }

                            .addOnFailureListener {

                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error if needed

                }
            })
    }






    /*
            READ FUNCTIONS
     */

    fun readYearList(): LiveData<List<String>> {
        val yearList = MutableLiveData<List<String>>()

        databaseReference.child(FirebaseUtils.firebaseUser!!.uid)
            .child("year").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val years = mutableListOf<String>()

                    if (snapshot.exists()) {
                        for (yearSnapshot in snapshot.children) {
                            val year = yearSnapshot.getValue(String::class.java)
                            year?.let { years.add(it) }
                        }
                    }

                    yearList.value = years
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error if needed

                }
            })

        return yearList
    }

    fun readOverviewData(): LiveData<StockInfo> {
        val overviewData = MutableLiveData<StockInfo>()

        databaseReference.child(FirebaseUtils.firebaseUser!!.uid)
            .child("overviewData").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val stockInfo = snapshot.getValue(StockInfo::class.java)
                        overviewData.value = stockInfo!!
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error if needed

                }
            })

        return overviewData
    }



    // read stocks list
    fun readStockList(): MutableLiveData<List<StockItemView>> {
        val stockList = MutableLiveData<List<StockItemView>>()

        stockRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val stocks = mutableListOf<StockItemView>()

                for (stockSnapshot in snapshot.children) {
                    val stock = stockSnapshot.getValue(StockItemView::class.java)
                    stock?.let { stocks.add(it) }
                }

                stockList.value = stocks
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed

            }
        })

        return stockList
    }

    // get Stock details


    /*
            UPDATE FUNCTIONS
     */


    /*
            DELETE FUNCTIONS
     */

    fun deleteStock(stock: Stock) {

        // delete form year wise list
        val year = stock.sellDate!!.substring(6, 10)
        userRef.child(FirebaseUtils.yearlyData).child(year).child(stock.id!!).removeValue()

        // delete from stock list
        stockRef.child(stock.name!!).child("list").child(stock.id!!).removeValue()
            .addOnSuccessListener {
                stockRef.child(stock.name!!).child(stockInfo).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val stockInfo = snapshot.getValue(StockInfo::class.java)
                        if (snapshot.exists()) {
                            val investedAmount = String.format(
                                "%.2f",
                                (stockInfo!!.investedAmount - stock.buyPrice * stock.units)
                            ).toDouble()

                            if (investedAmount == 0.0) {
                                stockRef.child(stock.name!!).removeValue()
                            } else {
                                val profit = String.format(
                                    "%.2f",
                                    (stockInfo.profitAmount - (stock.sellPrice - stock.buyPrice) * stock.units)
                                ).toDouble()
                                val profitGain =
                                    String.format("%.2f", (profit / investedAmount) * 100).toDouble()

                                val units = stockInfo.units - stock.units

                                val temp = StockInfo(
                                    stockInfo.stockName,
                                    investedAmount,
                                    profit,
                                    profitGain,
                                    units
                                )

                                stockRef.child(stock.name!!).child(FirebaseUtils.stockInfo).setValue(temp)
                                    .addOnSuccessListener {

                                    }
                                    .addOnFailureListener {

                                    }
                            }


                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error if needed

                    }
                })

            }
            .addOnFailureListener {

            }



        // delete from overview data
        userRef.child("overviewData").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val stockInfo = snapshot.getValue(StockInfo::class.java)
                if (snapshot.exists()) {
                    val investedAmount = String.format(
                        "%.2f",
                        (stockInfo!!.investedAmount - stock.buyPrice * stock.units)
                    ).toDouble()

                    if (investedAmount == 0.0) {
                        userRef.child("overviewData").removeValue()
                    } else {
                        val profit = String.format(
                            "%.2f",
                            (stockInfo.profitAmount - (stock.sellPrice - stock.buyPrice) * stock.units)
                        ).toDouble()
                        val profitGain =
                            String.format("%.2f", (profit / investedAmount) * 100).toDouble()

                        val units = stockInfo.units - stock.units

                        val temp = StockInfo(
                            stockInfo.stockName,
                            investedAmount,
                            profit,
                            profitGain,
                            units
                        )

                        userRef.child("overviewData").setValue(temp)
                            .addOnSuccessListener {

                            }
                            .addOnFailureListener {

                            }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }


}

