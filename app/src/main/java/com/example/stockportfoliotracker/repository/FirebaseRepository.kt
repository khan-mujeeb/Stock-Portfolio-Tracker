package com.example.stockportfoliotracker.repository

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import com.example.stockportfoliotracker.data.User
import com.example.stockportfoliotracker.utils.FirebaseUtils

class FirebaseRepository {

    val database = FirebaseUtils.firebaseDatabase

    //    add user
    fun addUser(
        user: User,
        phonenumber: String,
        currentContext: Context,
        GoToNextActivity: AppCompatActivity
    ) {
        database.reference.child("users").child(phonenumber).setValue(user).addOnSuccessListener {
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

}