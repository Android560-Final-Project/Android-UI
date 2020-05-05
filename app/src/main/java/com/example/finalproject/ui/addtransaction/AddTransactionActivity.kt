package com.example.finalproject.ui.addtransaction

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.finalproject.CurrencyExchangeService
import com.example.finalproject.R

import com.example.finalproject.db.*
import kotlinx.android.synthetic.main.add_transaction_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddTransactionActivity : Activity() {
    val TAG = "ADD_TRANSACTION"
    val BASE_URL = "https://exchangerateservice.firebaseapp.com/"
    val currencyList = listOf(CurrencyType.EUR.currencyCode, CurrencyType.JPY.currencyCode, CurrencyType.GBP.currencyCode, CurrencyType.USD.currencyCode)
    private lateinit var currencyApi: CurrencyExchangeService
    var accountCurrencyType: String = "USD"
    var exchangeRate = 1.0;
//    val accountDatabase = Room.databaseBuilder(requireContext(), CustomerDatabase::class.java, "customerDB").build()


    companion object {
        fun newInstance() = AddTransactionActivity()
    }

    private lateinit var viewModel: AddTransactionViewModel
    private lateinit var accountList: ArrayList<AccountEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_transaction_activity)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        currencyApi = retrofit.create(CurrencyExchangeService::class.java)

//        accountNames = accountDatabase.accountEntityDAO().getAllAccounts()


        accountList = ArrayList<AccountEntity>()
        accountList.add(AccountEntity("checking", "Account1", 100.0, CurrencyType.USD.currencyCode))
        accountList.add(AccountEntity("checking", "Account2", 100.0, CurrencyType.USD.currencyCode))

        val accountNames = ArrayList<String>()
        accountList.forEach {
            accountNames.add(it.name)
        }

        val accountNameAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, accountNames)
        account_spinner.adapter = accountNameAdapter
        account_spinner.onItemSelectedListener = accountSelected()

        val currencyTypeAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, currencyList)

        currency_type.adapter = currencyTypeAdapter
        currency_type.onItemSelectedListener = currencyTypeOnSelectedListener()

        currency_type.setSelection(currencyList.indexOf(accountCurrencyType))

        add_transaction.setOnClickListener{
            if (!transaction_amount.text.isNullOrBlank())
                Log.d(TAG, "${account_spinner.selectedItem} ${transaction_amount.text.toString().toDoubleOrNull()} from $accountCurrencyType to ${currency_type.selectedItem}}")
        }

        transaction_amount.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val amount = s.toString().toDouble()
                updateTotal(amount, exchangeRate)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

        })
    }

    private fun updateTotal(amount: Double, exchangeRate: Double) {
        total_cost.text = "Total Cost: ${(amount * exchangeRate)}"
    }

    private fun callExchangeRateApi(fromCurrency: String, toCurrency: String) {
        currencyApi.GetExchangeRate(fromCurrency, toCurrency)
            .enqueue(exchangeRateCallback())
    }

    private fun exchangeRateCallback(): Callback<Double> {
        return object: Callback<Double> {
            override fun onFailure(call: Call<Double>, t: Throwable) {
                Log.d(TAG, "onFailure: $t")
            }

            override fun onResponse(call: Call<Double>, response: Response<Double>) {
                exchangeRate = response.body() ?: 1.0
                exchange_rate.text = "1 ${currency_type.selectedItem.toString()} -> $exchangeRate $accountCurrencyType"
                if (!transaction_amount.text.toString().isNullOrBlank())
                {
                    val amount = transaction_amount.text.toString().toDouble()
                    updateTotal(amount, exchangeRate)
                }
            }
        }
    }

    fun addTransaction(view: View?) {
        if (!transaction_amount.text.isNullOrBlank())
            Log.d(TAG, "${account_spinner.selectedItem.toString()} $transaction_amount from $accountCurrencyType to ${currency_type.selectedItem.toString()} total: ${(transaction_amount.toString().toDouble() * exchangeRate).toString()}")
    }

    inner class currencyTypeOnSelectedListener: AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            Log.wtf(TAG, "Nothing is in the Exchange spinner")
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            Log.d(TAG, "currencyTypeOnSelectedListener $accountCurrencyType, ${parent?.getItemAtPosition(position).toString()}")
            callExchangeRateApi(parent?.getItemAtPosition(position).toString(), accountCurrencyType)
        }
    }

    inner class accountSelected: AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            Log.wtf(TAG, "Nothing is in the Account spinner")
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            var newAccount = accountList.find { account ->
                account.name == parent?.getItemAtPosition(position)
            }
            accountCurrencyType = newAccount!!.currency
            callExchangeRateApi(currency_type.selectedItem.toString(), accountCurrencyType)
        }

    }
}
