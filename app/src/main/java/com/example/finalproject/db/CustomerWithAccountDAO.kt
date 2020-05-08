package com.example.finalproject.db

import androidx.room.*

@Dao
interface CustomerWithAccountDAO {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(join: CustomerAccountCrossRef)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(join: List<CustomerAccountCrossRef>)

    @Transaction
    @Query("SELECT * FROM customer_table")
    fun getCustomersAccounts(): List<CustomerWithAccounts>

    @Transaction
    @Query("SELECT * FROM customer_table WHERE customerId = :customerId")
    fun getCustomerWithAccount(customerId: Int): CustomerWithAccounts

}