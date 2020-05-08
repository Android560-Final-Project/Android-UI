package com.example.finalproject.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.MainActivity
import com.example.finalproject.R
import com.example.finalproject.db.AccountEntity
import com.example.finalproject.db.CurrencyType
import com.example.finalproject.ui.addtransaction.AddTransaction
import com.example.finalproject.ui.transactions.ViewTransactions
import kotlinx.android.synthetic.main.account_item.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AccountsRecyclerAdapter(private val accounts: ArrayList<AccountEntity>, private val currencyLocale: Locale) : RecyclerView.Adapter<AccountsRecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.account_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val account = accounts[position]
        val currencyLocale = CurrencyType.values().find {
            it.currencyCode == account.currency
        }?.locale
        val currencyFormater = NumberFormat.getCurrencyInstance(currencyLocale)
        val accountBalance = currencyFormater.format(account.balance).toString()
        holder.accountName.text = account.name
        holder.balance.text = "Balance ${accountBalance}"
        holder.lastTransaction.text = accountBalance
        holder.addTransactionButton.setOnClickListener {
            val intent = Intent(it.context, AddTransaction::class.java)
            intent.putExtra("accountId", account.accountId)
            it.context.startActivity(intent)
        }
        holder.viewTransactionButton.setOnClickListener {
            val intent = Intent(it.context, ViewTransactions::class.java)
            intent.putExtra("AccountEntity", account)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return accounts.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val accountName = itemView.account_name
        val balance = itemView.balance
        val lastTransaction = itemView.last_transaction
        val viewTransactionButton = itemView.view_transaction_btn
        val addTransactionButton = itemView.add_transaction_btn
    }
}