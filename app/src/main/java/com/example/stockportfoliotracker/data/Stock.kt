package com.example.stockportfoliotracker.data

import android.os.Parcel
import android.os.Parcelable

data class Stock(
    var id: String? = "",
    val name: String? = "",
    val buyDate: String? = "",
    val sellDate: String? = "",
    val units: Int = 0,
    val buyPrice: Double = 0.0,
    val sellPrice: Double = 0.0
): Parcelable {
    constructor(parcel: Parcel) : this(

        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(buyDate)
        parcel.writeString(sellDate)
        parcel.writeInt(units)
        parcel.writeDouble(buyPrice)
        parcel.writeDouble(sellPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Stock> {
        override fun createFromParcel(parcel: Parcel): Stock {
            return Stock(parcel)
        }

        override fun newArray(size: Int): Array<Stock?> {
            return arrayOfNulls(size)
        }
    }
}
