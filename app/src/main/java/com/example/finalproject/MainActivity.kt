package com.example.finalproject

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.finalproject.ui.transactions.TransactionsViewModel

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: TransactionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(TransactionsViewModel::class.java)

        viewModel.accountId.value = 2


    }
}
