package com.example.finalproject.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.db.AccountEntity
import kotlinx.android.synthetic.main.account_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class AccountsRecyclerAdapter(private val accounts: ArrayList<AccountEntity>, private val currencyLocale: Locale) : RecyclerView.Adapter<AccountsRecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.account_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val account = accounts[position]
        holder.accountName.text = account.name
        holder.balance.text = "Balance ${account.balance}"
    }

    override fun getItemCount(): Int {
        return accounts.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val accountName = itemView.account_name
        val balance = itemView.balance
        val lastTransaction = itemView.last_transaction

        init {
            itemView.view_transaction_btn.setOnClickListener{
                // TODO -> navigate to transaction view
            }

            itemView.add_transaction_btn.setOnClickListener {
                // TODO -> navigate to add transaction view
            }
        }
    }
}