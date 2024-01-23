package com.example.stockportfoliotracker.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object utils {
    var sortBy: MutableLiveData<String> = MutableLiveData("profitAmount")

    var yearList: List<String> = listOf()

}