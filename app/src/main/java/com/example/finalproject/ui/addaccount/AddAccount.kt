package com.example.finalproject.ui.addaccount

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_add_account.*

class AddAccount : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)


    }

    fun cancel(view: View) {
        // go back to main activity
    }

    fun createAccount(view: View) {
        if (!editText.text.toString().isNullOrBlank() && !account_value.text.toString().isNullOrBlank()) {
            // add to db
            // go back to main activity
        }
        else {
            Toast.makeText(this, "Account value & Account anme must be filled out", Toast.LENGTH_SHORT)
        }
    }
}
