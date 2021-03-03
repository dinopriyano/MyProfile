package com.dupat.layouttest.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.dupat.layouttest.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

fun Context.toast(message: String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

fun Context.hideKeyboard(view: View) {

    var imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken,0)

}

fun Context.showKeyboard() {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.apply {
        toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
}

fun View.disable(){
    this.isEnabled = false
}

fun View.enable(){
    this.isEnabled = true
}

fun View.snackbar(msg: String)
{
    Snackbar.make(this,msg, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setActionTextColor(resources.getColor(R.color.orange))
        snackbar.setAction("OK"){
            snackbar.dismiss()
        }
    }.show()
}
