package com.example.finalproject.ui.transactions

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.finalproject.R
import com.example.finalproject.db.*
import kotlinx.android.synthetic.main.transactions_fragment.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class TransactionsFragment : Fragment() {
    private val TAG = "TransactionView"
    private var transactionList: List<TransactionEntity> = ArrayList()
    private lateinit var transactionViewModel: TransactionsViewModel
    private var accountLocale: Locale? = null
    private lateinit var accountDAO: AccountEntityDAO
    private lateinit var transactionDAO: TransactionEntityDAO

    companion object {
        fun newInstance() = TransactionsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accountDAO = CustomerDatabase.getInstance(requireContext()).accountEntityDAO()
        transactionDAO = CustomerDatabase.getInstance(requireContext()).transactionEntityDAO()
        return inflater.inflate(R.layout.transactions_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        transactionViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(TransactionsViewModel::class.java)
        transactionViewModel.accountId.observe(requireActivity(), Observer {
            thread {
                val account = accountDAO.getAccount(it)
                accountLocale = CurrencyType.values().find({ it.currencyCode == account.currency })?.locale

                // adds account value
                val currencyLocale = NumberFormat.getCurrencyInstance(accountLocale)
                account_balance.text = currencyLocale.format(account.balance)

                // adds account name
                account_name.text = account.name

                // adds transactions
                transactionList = transactionDAO.getAllTransactions(account.accountId)

                addAdapter(transactionList, accountLocale ?: Locale.US)
            }
        })
    }

    private fun addAdapter(transactions: List<TransactionEntity>, locale: Locale) {
        val adapter = TransactionsRecyclerAdapter(transactions, locale)
        transactions_recyler_view.adapter = adapter
        transactions_recyler_view.layoutManager = LinearLayoutManager(context)
    }

}
