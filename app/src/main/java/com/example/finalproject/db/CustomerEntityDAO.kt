package com.example.finalproject.db

import androidx.room.*

@Dao
interface CustomerEntityDAO {
    @Insert
    fun addCustomer(customer: CustomerEntity): Long

    @Update
    fun updateCustomer(customer: CustomerEntity)

    @Delete
    fun deleteCustomer(customer: CustomerEntity)

    @Query("SELECT * FROM customer_table WHERE email = :email")
    fun getCustomer(email: String): CustomerEntity
}