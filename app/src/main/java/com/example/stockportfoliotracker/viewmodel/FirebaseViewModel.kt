package com.example.stockportfoliotracker.viewmodel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.stockportfoliotracker.data.User
import com.example.stockportfoliotracker.repository.FirebaseRepository

class FirebaseViewModel(
    val repository: FirebaseRepository = FirebaseRepository()
): ViewModel() {
    fun addUser(user: User, phonenumber: String, currentContext: Context, GoToNextActivity: AppCompatActivity) {
        repository.addUser(user, phonenumber, currentContext, GoToNextActivity)
    }

}