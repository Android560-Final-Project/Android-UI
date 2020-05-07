package com.example.finalproject.db

import androidx.room.Entity

@Entity(primaryKeys = ["customerId", "accountId"])
data class CustomerAccountCrossRef(
    val customerId: Int,
    val accountId: Int
)