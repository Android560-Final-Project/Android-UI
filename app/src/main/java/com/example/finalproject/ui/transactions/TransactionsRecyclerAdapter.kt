package com.example.finalproject.ui.transactions

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.db.TransactionEntity
import kotlinx.android.synthetic.main.row_item.view.*
import java.text.NumberFormat
import java.util.*

class TransactionsRecyclerAdapter(private val transactions: ArrayList<TransactionEntity>, private val currencyLocale: Locale): RecyclerView.Adapter<TransactionsRecyclerAdapter.MyViewHolder>() {
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
        val currencyFormat = NumberFormat.getCurrencyInstance(currencyLocale)

        val currentTransaction = transactions[position]
        holder.transactionName.text = currentTransaction.transactionName
        if (currentTransaction.amount > 0) {
            holder.amount.text = currencyFormat.format(currentTransaction.amount)
            holder.amount.setTextColor(Color.GREEN)
        }
        else {
            holder.amount.setTextColor(Color.RED)
            holder.amount.text = currencyFormat.format(currentTransaction.amount * -1)
        }
        holder.date.text = currentTransaction.date.toString()
        if (position % 2 == 1)
            holder.itemView.setBackgroundColor(Color.LTGRAY)
        else
            holder.itemView.setBackgroundColor(Color.WHITE)
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