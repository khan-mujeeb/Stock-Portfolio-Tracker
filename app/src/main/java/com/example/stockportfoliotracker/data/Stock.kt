package com.example.stockportfoliotracker.data

data class Stock(
    val name: String = "",
    val buyDate: String = "",
    val sellDate: String = "",
    val units: Int = 0,
    val buyPrice: Double = 0.0,
    val sellPrice: Double = 0.0
)
