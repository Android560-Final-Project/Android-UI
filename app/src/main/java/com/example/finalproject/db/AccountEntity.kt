package com.example.finalproject.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "account_table")
data class AccountEntity(
    var customerId: Int,
    var type: String,
    var name: String,
    var balance: Double,
    var currency: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var accountId: Int = 0
    override fun toString():String {
        return name
    }
}