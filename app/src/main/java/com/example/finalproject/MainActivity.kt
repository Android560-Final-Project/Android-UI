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
    private lateinit var customerAccountCrossRef: CustomerWithAccountDAO
    private var customerId: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        accounts = ArrayList()
        accountDAO = CustomerDatabase.getInstance(this).accountEntityDAO()
        customerDAO = CustomerDatabase.getInstance(this).customerEntityDAO()
        customerAccountCrossRef = CustomerDatabase.getInstance(this).customerWithAccountDAO()
        addAdapter(Locale.ENGLISH)
        init()
    }

    private fun addAdapter(locale: Locale) {
        val adapter = AccountsRecyclerAdapter(accounts, locale)
        accounts_recycler_view.adapter = adapter
        accounts_recycler_view.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        thread {
            val customer = customerDAO.getCustomer(exampleEmail)
            if(customer != null) {
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
            var message: String =""

            if(customer == null) {
                // insert the first entities into the db

                Log.d(TAG, "Customer with email $exampleEmail does not exist. Adding to DB")
                val customerEntity = CustomerEntity("example name", exampleEmail, "860-371-8881")
                val accountEntity = AccountEntity("checkings", "myaccount", 300.00, "USD")
                customerDAO.addCustomer(customerEntity)
                val account1Id = accountDAO.addAccount(accountEntity)
                val account2Id = accountDAO.addAccount(accountEntity)
                // join relationship
                customerAccountCrossRef.insert(CustomerAccountCrossRef(customerId, account1Id.toInt()))
                customerAccountCrossRef.insert(CustomerAccountCrossRef(customerId, account2Id.toInt()))
                accounts.add(accountEntity)
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
        val customerWithAccount = customerAccountCrossRef.getCustomerWithAccount(customerId)
        if(customerWithAccount != null) {
            accounts.clear()
            accounts.addAll(customerWithAccount.accounts)

        }
    }

    fun addAccount(view: View?) {
        val intent = Intent(this, AddAccount::class.java)
        intent.putExtra("customerId", this.customerId)
        startActivity(intent)
    }
}
