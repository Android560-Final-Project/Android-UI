package com.example.finalproject.db

import androidx.room.*

@Dao
interface CustomerEntityDAO {
    @Insert
    fun addCustomer(customer: CustomerEntity)

    @Update
    fun updateCustomer(customer: CustomerEntity)

    @Delete
    fun deleteCustomer(customer: CustomerEntity)

    @Transaction
    @Query("SELECT * FROM customer_table")
    fun getCustomerWithAccounts(): List<CustomerWithAccounts>
}