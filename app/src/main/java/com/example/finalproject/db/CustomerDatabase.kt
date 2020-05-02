package com.example.finalproject.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [AccountEntity::class,
                        CustomerEntity::class,
                        CustomerAccountCrossRef::class,
                        TransactionEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class CustomerDatabase : RoomDatabase() {

    abstract fun accountEntityDAO(): AccountEntityDAO

    abstract fun customerEntityDAO(): CustomerEntityDAO

    abstract fun transactionEntityDAO(): TransactionEntityDAO
}