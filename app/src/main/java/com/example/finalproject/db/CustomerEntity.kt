package com.example.finalproject.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_table")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    var customerId: Int,
    var name: String,
    var email: String,
    var phone: String
)