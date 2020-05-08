package com.example.finalproject.db

import androidx.room.*

@Dao
interface AccountEntityDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAccount(account: AccountEntity): Long

    @Update
    fun updateAccount(account: AccountEntity)

    @Delete
    fun deleteAccount(account: AccountEntity)

    @Query("SELECT * FROM account_table")
    fun getAllAccounts(): List<AccountEntity>

    @Query("SELECT * FROM account_table WHERE accountId = :accountId")
    fun getAccount(accountId: Int): AccountEntity

    @Query("SELECT * FROM account_table WHERE customerId = :customerId")
    fun getAccountByCustomerId(customerId: Int): List<AccountEntity>
}