package com.example.finalproject.db

import androidx.room.*

@Dao
interface AccountEntityDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAccount(account: AccountEntity)

    @Update
    fun updateAccount(account: AccountEntity)

    @Delete
    fun deleteAccount(account: AccountEntity)

    @Query("SELECT * FROM account_table")
    fun getAllAccounts(): List<AccountEntity>
}