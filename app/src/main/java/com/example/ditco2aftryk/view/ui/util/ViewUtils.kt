package com.example.ditco2aftryk.view.ui.util

import android.content.Context
import android.widget.Toast

fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    // where it is needed, write: toast("some text")
}