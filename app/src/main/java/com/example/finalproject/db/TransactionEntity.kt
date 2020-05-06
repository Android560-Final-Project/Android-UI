package com.example.finalproject.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "transaction_table")
data class TransactionEntity(
    var accountId: Int,
    var amount: Double,
    var date: Date,
    var isDeposit: Boolean,
    var transactionName: String
) {
    @PrimaryKey(autoGenerate = true)
    var transactionId: Int = 0
}