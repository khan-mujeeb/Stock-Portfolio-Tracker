package com.example.stockportfoliotracker.viewmodel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stockportfoliotracker.data.StockItemView
import com.example.stockportfoliotracker.data.User
import com.example.stockportfoliotracker.repository.FirebaseRepository

class FirebaseViewModel(
    val repository: FirebaseRepository = FirebaseRepository()
): ViewModel() {

    fun readStockList(): MutableLiveData<List<StockItemView>> {
        return repository.readStockList()
    }
    fun addUser(user: User, phonenumber: String, currentContext: Context, GoToNextActivity: AppCompatActivity) {
        repository.addUser(user, phonenumber, currentContext, GoToNextActivity)
    }



}