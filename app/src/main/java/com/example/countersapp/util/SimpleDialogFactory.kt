package com.example.countersapp.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

typealias ButtonAction = (DialogInterface, Int) -> Unit
typealias Button = Pair<String, ButtonAction>

object SimpleDialogFactory {
    val noAction: (DialogInterface, Int) -> Unit = { _, _ -> }
    fun createDialog(
        context: Context,
        title: String? = null,
        message: String? = null,
        positiveButton: Button? = null,
        negativeButton: Button? = null
    ): AlertDialog {
        return AlertDialog.Builder(context).create().apply {
            title?.let { setTitle(it) }
            message?.let { setMessage(it) }
            positiveButton?.let {
                setButton(AlertDialog.BUTTON_POSITIVE, it.first, it.second)
            }
            negativeButton?.let {
                setButton(AlertDialog.BUTTON_NEGATIVE, it.first, it.second)
            }
        }
    }
}