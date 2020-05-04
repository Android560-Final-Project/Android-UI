package com.example.finalproject.ui.addtransaction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.db.CurrencyType

class AddTransactionViewModel : ViewModel() {
    val accountDetails = MutableLiveData<AccountDetails>()
    // TODO: Implement the ViewModel
}

data class AccountDetails(val accountNumber: Int, val currencyType: CurrencyType)
