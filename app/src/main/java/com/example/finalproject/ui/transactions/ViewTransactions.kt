package com.example.finalproject.ui.transactions

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.finalproject.R
import com.example.finalproject.db.*
import kotlinx.android.synthetic.main.activity_view_transactions.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class ViewTransactions : AppCompatActivity() {
    private val TAG = "ViewTransactions"
    private var transactionList: List<TransactionEntity> = ArrayList()
    private var accountLocale: Locale? = null
    private lateinit var accountDAO: AccountEntityDAO
    private lateinit var transactionDAO: TransactionEntityDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_transactions)
        accountDAO = CustomerDatabase.getInstance(this).accountEntityDAO()
        transactionDAO = CustomerDatabase.getInstance(this).transactionEntityDAO()
        val account = intent.getSerializableExtra("AccountEntity") as AccountEntity
        Log.d(TAG, "Account" + account.accountId)
        init(account)
    }

    private fun init(account: AccountEntity) {
        thread {

            accountLocale = CurrencyType.values().find({ it.currencyCode == account.currency })?.locale

            // adds account value
            val currencyLocale = NumberFormat.getCurrencyInstance(accountLocale)
            account_balance.text = currencyLocale.format(account.balance)

            // adds account name
            account_name.text = account.name

            // adds transactions
            transactionList = transactionDAO.getAllTransactions(account.accountId)


            addAdapter(transactionList.reversed(), accountLocale ?: Locale.US)
        }
    }

    private fun addAdapter(transactions: List<TransactionEntity>, locale: Locale) {
        val adapter = TransactionsRecyclerAdapter(transactions, locale)
        transactions_recyler_view.adapter = adapter
        transactions_recyler_view.layoutManager = LinearLayoutManager(this)
    }

}
