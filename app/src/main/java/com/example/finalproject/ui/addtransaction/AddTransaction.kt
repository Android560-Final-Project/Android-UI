package com.example.finalproject.ui.addtransaction

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.finalproject.CurrencyExchangeService
import com.example.finalproject.MainActivity
import com.example.finalproject.R
import com.example.finalproject.db.*
import kotlinx.android.synthetic.main.activity_add_transaction.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class AddTransaction : Activity() {
    val TAG = "ADD_TRANSACTION"
    val BASE_URL = "https://exchangerateservice.firebaseapp.com/"
    private lateinit var currencyApi: CurrencyExchangeService
    var accountCurrencyType: String = "USD"
    var exchangeRate = 1.0;
    lateinit var accountDao: AccountEntityDAO
    lateinit var transactionDao: TransactionEntityDAO

    private var accountList: List<AccountEntity> = ArrayList<AccountEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        accountDao = CustomerDatabase.getInstance(this).accountEntityDAO()
        transactionDao = CustomerDatabase.getInstance(this).transactionEntityDAO()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        currencyApi = retrofit.create(CurrencyExchangeService::class.java)

        initAccountSpinner()

        val currencyList = resources.getStringArray(R.array.CurrencyTypes)
        currency_type.setSelection(currencyList.indexOf(accountCurrencyType))
        currency_type.onItemSelectedListener = currencyTypeOnSelectedListener()

        transaction_amount.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val amount = s.toString().toDouble()
                updateTotal(amount, exchangeRate)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun initAccountSpinner() {
        thread{
            accountList = accountDao.getAllAccounts()

            val accountNameAdapter = ArrayAdapter<AccountEntity>(this, android.R.layout.simple_spinner_dropdown_item, accountList)
            account_spinner.adapter = accountNameAdapter
            account_spinner.onItemSelectedListener = accountSelected()
        }
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
        {
            var account = account_spinner.selectedItem as AccountEntity
            var totalAmount = transaction_amount.text.toString().toDouble() * exchangeRate
            var transactionName = transaction_name.text.toString()
            var isDeposit = deposit.isChecked

            val transaction = TransactionEntity(account.accountId, totalAmount * exchangeRate, Date(), isDeposit, transactionName)
            thread {
                transactionDao.addTransaction(transaction)
                if (isDeposit)
                    account.balance = account.balance + totalAmount
                else
                    account.balance = account.balance - totalAmount
                accountDao.updateAccount(account)
                Log.d(TAG, transactionDao.getAllTransactions(account.accountId).toString())
            }
        }
    }

    fun cancel(view: View?){
        val myIntent = Intent(this, MainActivity::class.java)
        setResult(Activity.RESULT_CANCELED, myIntent)
        finish()
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
            var newAccount = parent?.getItemAtPosition(position) as AccountEntity
            accountCurrencyType = newAccount!!.currency
            callExchangeRateApi(currency_type.selectedItem.toString(), accountCurrencyType)
        }

    }
}
