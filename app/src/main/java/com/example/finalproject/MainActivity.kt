package com.example.finalproject

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.db.AccountEntity
import com.example.finalproject.db.CustomerAccountCrossRef
import com.example.finalproject.db.CustomerDatabase
import com.example.finalproject.db.CustomerEntity
import com.example.finalproject.ui.home.AccountsRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val exampleEmail = "example@gmail.com"
    private lateinit var accounts: ArrayList<AccountEntity>

//    lateinit var viewModel: TransactionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        accounts = ArrayList()
        addAdapter(Locale.ENGLISH)
        init()
//        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(TransactionsViewModel::class.java)
//
//        viewModel.accountId.value = 2


    }

    private fun addAdapter(locale: Locale) {
        val adapter = AccountsRecyclerAdapter(accounts, locale)
        accounts_recycler_view.adapter = adapter
        accounts_recycler_view.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        // to
    }

    private fun init() {
        val accountDAO = CustomerDatabase.getInstance(this).accountEntityDAO()
        val customerDAO = CustomerDatabase.getInstance(this).customerEntityDAO()
        val customerAccountCrossRef = CustomerDatabase.getInstance(this).customerWithAccountDAO()

        thread {
            val customer = customerDAO.getCustomer(exampleEmail)
            var message: String =""

            if(customer == null) {
                // insert the first entities into the db

                Log.d(TAG, "Customer with email $exampleEmail does not exist. Adding to DB")
                val customerEntity = CustomerEntity("example name", exampleEmail, "860-371-8881")
                val accountEntity = AccountEntity("checkings", "myaccount", 300.00, "USD")
                customerDAO.addCustomer(customerEntity)
                accountDAO.addAccount(accountEntity)
                // join relationship
                customerAccountCrossRef.insert(CustomerAccountCrossRef(customerEntity.customerId, accountEntity.accountId))
                accounts.add(accountEntity)
                message = "Welcome new customer ${customerEntity.name}!!"
            } else {
                Log.d(TAG, "Customer with $exampleEmail already exists. Customer id ${customer.customerId}")
                val customerWithAccount = customerAccountCrossRef.getCustomerWithAccount(customer.customerId)
                if(customerWithAccount != null) {
                    accounts.addAll(customerWithAccount.accounts)
                    message = "Welcome ${customer.name}"
                }
            }

            runOnUiThread{
                accounts_recycler_view.adapter?.notifyDataSetChanged()
                display_message.text = message
            }
        }
    }
}
