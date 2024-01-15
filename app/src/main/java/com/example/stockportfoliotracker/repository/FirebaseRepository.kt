package com.example.stockportfoliotracker.repository

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stockportfoliotracker.data.StockItemView
import com.example.stockportfoliotracker.data.User
import com.example.stockportfoliotracker.utils.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class FirebaseRepository {

    val databaseReference = FirebaseUtils.firebaseDatabase.reference
    val stockRef = databaseReference.child(FirebaseUtils.firebaseUser!!.uid).child("stocks")
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



}