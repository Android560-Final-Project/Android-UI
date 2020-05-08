package com.example.finalproject.ui.addaccount

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.finalproject.R
import com.example.finalproject.db.*
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlin.concurrent.thread

class AddAccount : AppCompatActivity() {
    private val TAG = "ADD_ACCOUNT"
    private lateinit var accountDAO : AccountEntityDAO
    private var customerId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)
        accountDAO = CustomerDatabase.getInstance(this).accountEntityDAO()
        customerId = intent.getIntExtra("customerId", -1)
        Log.d(TAG, customerId.toString())
    }

    private fun init() {

    }

    fun cancel(view: View) {
        // go back to main activity
        val myIntent = Intent()
        setResult(Activity.RESULT_CANCELED, myIntent)
        finish()
    }

    fun createAccount(view: View) {
        if (!editText.text.toString().isNullOrBlank() && !account_value.text.toString().isNullOrBlank()) {

            // add to db
            val name = editText.text.toString()
            val balance = account_value.text.toString().toDouble()
            val type = account_type.selectedItem.toString()
            val currency = currency_type.selectedItem.toString()
            val account = AccountEntity(customerId, type, name, balance, currency)

            thread {
                accountDAO.addAccount(account) // go back to main activity
            }
        }
        else {
            Toast.makeText(this, "Account value & Account name must be filled out", Toast.LENGTH_SHORT).show()
        }
    }
}
