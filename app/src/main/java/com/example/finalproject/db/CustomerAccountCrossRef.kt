package com.example.finalproject.db

import androidx.room.Entity

@Entity(primaryKeys = ["accountId", "customerId"])
data class CustomerAccountCrossRef(
    val accountId: Int,
    val customerId: Int
)