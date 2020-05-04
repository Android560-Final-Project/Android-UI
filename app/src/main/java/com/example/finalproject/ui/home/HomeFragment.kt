package com.example.finalproject.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.finalproject.R
import com.example.finalproject.db.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private val exampleEmail = "example@gmail.com"
    private lateinit var accounts: ArrayList<AccountEntity>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        accounts = ArrayList()
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addAdapter(Locale.ENGLISH)
        init()
    }

    private fun addAdapter(locale: Locale) {
        val adapter = AccountsRecyclerAdapter(accounts, locale)
        accounts_recycler_view.adapter = adapter
        accounts_recycler_view.layoutManager = LinearLayoutManager(context)
    }

    private fun init() {
        context?.let {
            val accountDAO = CustomerDatabase.getInstance(it).accountEntityDAO()
            val customerDAO = CustomerDatabase.getInstance(it).customerEntityDAO()
            val customerAccountCrossRef = CustomerDatabase.getInstance(it).customerWithAccountDAO()

            thread {
                val customer = customerDAO.getCustomer(exampleEmail)
                var message: String =""

                if(customer == null) {
                    // insert the first entities into the db

                    Log.d(TAG, "Customer with email ${exampleEmail} does not exist. Adding to DB")
                    val customerEntity = CustomerEntity("example name", exampleEmail, "860-371-8881")
                    val accountEntity = AccountEntity("checkings", "myaccount", 300.00, "USD")
                    customerDAO.addCustomer(customerEntity)
                    accountDAO.addAccount(accountEntity)
                    // join relationship
                    customerAccountCrossRef.insert(CustomerAccountCrossRef(customerEntity.customerId, accountEntity.accountId))
                    accounts.add(accountEntity)
                    message = "Welcome new customer ${customerEntity.name}!!"
                } else {
                    Log.d(TAG, "Customer with ${exampleEmail} already exists. Customer id ${customer.customerId}")
                    val customerWithAccount = customerAccountCrossRef.getCustomerWithAccount(customer.customerId)
                    if(customerWithAccount != null) {
                        accounts.addAll(customerWithAccount.accounts)
                        message = "Welome ${customer.name}"
                    }
                }

                activity?.runOnUiThread{
                    accounts_recycler_view.adapter?.notifyDataSetChanged()
                    display_message.text = message
                }
            }
        }
    }
}
