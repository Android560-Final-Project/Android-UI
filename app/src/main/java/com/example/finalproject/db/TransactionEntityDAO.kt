package com.example.finalproject.db

import androidx.room.*

@Dao
interface TransactionEntityDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTransaction(transaction: TransactionEntity)

    @Delete
    fun deleteTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transaction_table WHERE transactionId = :accountId")
    fun getAllTransactions(accountId: Int): List<TransactionEntity>
}