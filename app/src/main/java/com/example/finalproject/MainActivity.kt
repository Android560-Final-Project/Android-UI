package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.db.*
import com.example.finalproject.ui.addaccount.AddAccount
import com.example.finalproject.ui.home.AccountsRecyclerAdapter
import com.example.finalproject.ui.transactions.ViewTransactions
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val exampleEmail = "example@gmail.com"
    private lateinit var accounts: ArrayList<AccountEntity>
    private lateinit var accountDAO: AccountEntityDAO
    private lateinit var customerDAO: CustomerEntityDAO
    private var customerId: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        accounts = ArrayList()
        accountDAO = CustomerDatabase.getInstance(this).accountEntityDAO()
        customerDAO = CustomerDatabase.getInstance(this).customerEntityDAO()
        addAdapter(Locale.ENGLISH)
        init()
    }

    private fun addAdapter(locale: Locale) {
        val adapter = AccountsRecyclerAdapter(accounts, locale)
        accounts_recycler_view.adapter = adapter
        accounts_recycler_view.layoutManager = LinearLayoutManager(this)
    }

    override fun onRestart() {
        super.onRestart()
        thread {
            val accounts = accountDAO.getAllAccounts()
            for(account in accounts) {
                Log.d(TAG, "ALL Accounts $account")
            }
            Log.d(TAG, "ALL accounts ${accountDAO.getAllAccounts()}")
            val customer = customerDAO.getCustomer(exampleEmail)
            if(customer != null) {
                Log.d(TAG, "Update customer $customer")
                updateAccounts(customer.customerId)
            }
            runOnUiThread{
                accounts_recycler_view.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun init() {
        thread {
            var customer = customerDAO.getCustomer(exampleEmail)
            customerId = customer.customerId
            var message: String =""

            if(customer == null) {
                // insert the first entities into the db

                Log.d(TAG, "Customer with email $exampleEmail does not exist. Adding to DB")
                val customerEntity = CustomerEntity("example name", exampleEmail, "860-371-8881")
                customerId = customerDAO.addCustomer(customerEntity).toInt()
                val accountEntity = AccountEntity(customerId,"checkings", "myaccount", 300.00, "USD")
                val accountEntity2 = AccountEntity(customerId,"savings", "my-retirement-fund", 300.00, "USD")

                val account1Id = accountDAO.addAccount(accountEntity)
                val account2Id = accountDAO.addAccount(accountEntity2)

                Log.d(TAG, "account1 $account1Id")
                Log.d(TAG, "account2 $account2Id")

                Log.d(TAG, "Accounts ${accountDAO.getAccountByCustomerId(customerId)}")
                updateAccounts(customerId)
                message = "Welcome new customer ${customerEntity.name}!!"
            } else {
                Log.d(TAG, "Customer with $exampleEmail already exists. Customer id $customerId")
                message = "Welcome ${customer.name}"
                updateAccounts(customerId)
            }

            runOnUiThread{
                accounts_recycler_view.adapter?.notifyDataSetChanged()
                display_message.text = message
            }
        }
    }

    private fun updateAccounts(customerId: Int) {
        val customerAccounts = accountDAO.getAccountByCustomerId(customerId)
        Log.d(TAG, "Customer Accounts $customerAccounts")
        accounts.clear()
        accounts.addAll(customerAccounts)

    }

    fun addAccount(view: View?) {
        val intent = Intent(this, AddAccount::class.java)
        intent.putExtra("customerId", this.customerId)
        startActivity(intent)
    }
}
