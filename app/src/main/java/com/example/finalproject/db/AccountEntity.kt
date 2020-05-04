package com.example.finalproject.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_table")
data class AccountEntity(
    var type: String,
    var name: String,
    var balance: Double,
    var currency: String
){
    @PrimaryKey(autoGenerate = true)
    var accountId: Int = 0
}