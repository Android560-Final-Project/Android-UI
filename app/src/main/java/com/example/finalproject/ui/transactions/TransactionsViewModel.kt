package com.example.finalproject.ui.transactions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.db.TransactionEntity
import java.util.*
import kotlin.collections.ArrayList

class TransactionsViewModel : ViewModel() {
    val accountId = MutableLiveData<Int>()
}
