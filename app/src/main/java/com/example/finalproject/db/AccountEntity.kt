package com.example.finalproject.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_table")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    var accountId: Int,
    var type: String,
    var name: String
)