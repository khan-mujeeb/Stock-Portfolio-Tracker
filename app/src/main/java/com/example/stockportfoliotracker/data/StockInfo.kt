package com.example.stockportfoliotracker.data

data class StockInfo(
    val stockName: String = "",
    val investedAmount: Double = 0.0,
    val profitAmount: Double = 0.0,
    val profitGain: Double = 0.0
)