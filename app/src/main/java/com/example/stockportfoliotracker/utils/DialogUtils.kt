package com.example.stockportfoliotracker.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.stockportfoliotracker.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogUtils {
    fun buildLoadingDialog(context: Context): AlertDialog {
        val builder = MaterialAlertDialogBuilder(
            context
        )
        builder
            .setMessage("Loading...")
        builder.setCancelable(false)
        return builder.create()
    }
}