package com.transactiononyourtips

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.transactiononyourtips.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var TransactionsAdapt: TransactionsAdapter
    private lateinit var transactions: ArrayList<Transactions>
    private lateinit var linearLayoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        transactions = arrayListOf(
            Transactions("Tea",-60.05,"Evening Tea","09/10/2024"),
            Transactions("Coffee",-150.00,"Evening Coffee","09/10/2024"),
            Transactions("Salary",100000.00,"This Month Salary","09/10/2024"),
            Transactions("Lunch",-600.05,"Lunch","09/10/2024")
        )

        TransactionsAdapt = TransactionsAdapter(transactions, this)
        linearLayoutManager = LinearLayoutManager(this)

        binding.recyclerview.apply {
            adapter = TransactionsAdapt
            layoutManager = linearLayoutManager
        }
        addTransaction()
    }
    private fun addTransaction() {
        val but = binding.addBtn
        but.setOnClickListener {
            val intent = Intent(this, TransactionActivity::class.java)
            startActivity(intent)
        }
    }

}