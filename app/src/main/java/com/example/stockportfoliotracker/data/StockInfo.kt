package com.example.stockportfoliotracker.data

import android.os.Parcel
import android.os.Parcelable

data class StockInfo(
    val stockName: String? = "",
    val investedAmount: Double = 0.0,
    val profitAmount: Double = 0.0,
    val profitGain: Double = 0.0,
    val units: Int = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(stockName)
        parcel.writeDouble(investedAmount)
        parcel.writeDouble(profitAmount)
        parcel.writeDouble(profitGain)
        parcel.writeInt(units)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StockInfo> {
        override fun createFromParcel(parcel: Parcel): StockInfo {
            return StockInfo(parcel)
        }

        override fun newArray(size: Int): Array<StockInfo?> {
            return arrayOfNulls(size)
        }
    }
}
