package com.example.finalproject.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CustomerWithAccounts(
    @Embedded val customer: CustomerEntity,
    @Relation(
        parentColumn = "customerId",
        entityColumn = "accountId",
        entity = AccountEntity::class
    )
    val accounts: List<AccountEntity>
)