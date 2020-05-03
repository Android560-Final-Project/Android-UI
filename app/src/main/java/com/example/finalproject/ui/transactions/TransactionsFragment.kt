package com.example.finalproject.ui.transactions

import android.os.Bundle
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
import com.example.finalproject.db.TransactionEntity
import kotlinx.android.synthetic.main.transactions_fragment.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionsFragment : Fragment() {
    private val transactionList: ArrayList<TransactionEntity> = ArrayList()

    companion object {
        fun newInstance() = TransactionsFragment()
    }

    private lateinit var transactionViewModel: TransactionsViewModel
    private var accountLocale: Locale = Locale.US

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.transactions_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        transactionViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(TransactionsViewModel::class.java)
        transactionViewModel.transactions.observe(requireActivity(), Observer {
            transactionList.addAll(it)
        })
        transactionViewModel.currencyLocale.observe(requireActivity(), Observer {
            accountLocale = it
        })
        transactionViewModel.accountName.observe(requireActivity(), Observer {
            account_name.text = it
        })
        transactionViewModel.accountValue.observe(requireActivity(), Observer {
            val currencyLocale = NumberFormat.getCurrencyInstance(accountLocale)
            account_balance.text = currencyLocale.format(it)
        })

        val adapter = TransactionsRecyclerAdapter(transactionList, accountLocale);
        transactions_recyler_view.adapter = adapter
        transactions_recyler_view.layoutManager = LinearLayoutManager(context)
    }

}
