package com.example.stockportfoliotracker.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object FirebaseUtils {
    var firebaseAuth = FirebaseAuth.getInstance()
    var firebaseUser = firebaseAuth.currentUser
    var firebaseDatabase = FirebaseDatabase.getInstance()

    var phoneNumber = firebaseUser!!.phoneNumber
}