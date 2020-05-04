package com.example.finalproject.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.R
import com.example.finalproject.db.AccountEntity
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO -> fetch account entities from DB
        val account = AccountEntity(1, "checkings", "myaccount", 300.00, "USD")
        val accounts = ArrayList<AccountEntity>()
        accounts.add(account)
        addAdapter(accounts, Locale.ENGLISH)
    }

    private fun addAdapter(accounts: ArrayList<AccountEntity>, locale: Locale) {
        val adapter = AccountsRecyclerAdapter(accounts, locale)
        accounts_recycler_view.adapter = adapter
        accounts_recycler_view.layoutManager = LinearLayoutManager(context)
    }
}
