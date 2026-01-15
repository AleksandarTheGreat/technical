package com.example.technical.Helper

import android.content.Context
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object Toaster {

    fun text(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    fun alert(context: Context, message: String){
        val builder = MaterialAlertDialogBuilder(context);
        builder.setTitle("Alert")
            .setMessage(message)
            .setCancelable(true)
            .show();
    }

    fun alert(context: Context, title: String, message: String){
        val builder = MaterialAlertDialogBuilder(context);
        builder.setTitle(title)
            .setMessage(message)
            .setCancelable(true)
            .show();
    }

}