package com.example.finalproject.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.finalproject.SingletonHolder

@Database(entities = [AccountEntity::class,
                        CustomerEntity::class,
                        TransactionEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class CustomerDatabase : RoomDatabase() {

    abstract fun accountEntityDAO(): AccountEntityDAO

    abstract fun customerEntityDAO(): CustomerEntityDAO

    abstract fun transactionEntityDAO(): TransactionEntityDAO

    companion object : SingletonHolder<CustomerDatabase, Context>({
        Room.databaseBuilder(it.applicationContext,
                CustomerDatabase::class.java,
                "customer.db").build()
    })
}