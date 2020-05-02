package com.example.finalproject.ui.transactions

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.db.CurrencyTypes
import com.example.finalproject.db.TransactionEntity
import kotlinx.android.synthetic.main.row_item.view.*
import java.text.NumberFormat

class TransactionsRecyclerAdapter(private val transactions: ArrayList<TransactionEntity>): RecyclerView.Adapter<TransactionsRecyclerAdapter.MyViewHolder>() {
    private val TAG = "TransactionsRecycler"
    // inflate layout from row_item.xml and return the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currency = CurrencyTypes.values().find { type -> type.currencyCode == "US" }
        val currencyFormat = NumberFormat.getCurrencyInstance(currency?.locale)

        val currentTransaction = transactions[position]
        holder.transactionName.text = currentTransaction.transactionName
        holder.amount.text = currencyFormat.format(currentTransaction.amount)
        holder.date.text = currentTransaction.date.toString()
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val transactionName = itemView.transaction_name
        val amount = itemView.transaction_amount
        val date = itemView.transaction_date

        init {
//            itemView.setOnLongClickListener{
//                // editFragment???
//            }
        }
    }
}