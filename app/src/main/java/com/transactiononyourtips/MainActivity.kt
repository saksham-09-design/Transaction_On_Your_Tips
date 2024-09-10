package com.transactiononyourtips

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.transactiononyourtips.databinding.ActivityMainBinding
import kotlin.math.abs

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
        allTransactions()
        updateDashboard()
    }

    private fun updateDashboard() {
        val totalAmt = transactions.map{ it.amt }.sum()
        val totalInc = transactions.filter{ it.amt >= 0 }.map{ it.amt }.sum()
        val totalExp = transactions.filter{ it.amt < 0 }.map{ it.amt }.sum()
        val minSp = transactions.filter{it.amt < 0}.map{it.amt}.maxOrNull()?:0.0
        val maxSp = transactions.filter{it.amt < 0}.map{it.amt}.minOrNull()?:0.0
        val avgSp = transactions.filter{it.amt < 0}.map{it.amt}.average()
        val maxIn = transactions.filter{it.amt > 0}.map{it.amt}.maxOrNull()?:0.0
        val minIn = transactions.filter{it.amt > 0}.map{it.amt}.minOrNull()?:0.0
        val avgIn = transactions.filter{it.amt > 0}.map{it.amt}.average()

        if(totalAmt >=0)
            binding.balance.setTextColor(getResources().getColor(R.color.green))
        else
            binding.balance.setTextColor(getResources().getColor(R.color.red))
        binding.balance.text = "₹%.2f".format(totalAmt)
        binding.income.text = "₹%.2f".format(totalInc)
        binding.expense.text = "-₹%.2f".format(abs(totalExp))
        binding.MinSpAmt.text = "-₹%.2f".format(abs(minSp))
        binding.MaxSpAmt.text = "-₹%.2f".format(abs(maxSp))
        binding.AvgSpAmt.text = "-₹%.2f".format(abs(avgSp))
        binding.MinInAmt.text = "₹%.2f".format(minIn)
        binding.MaxInAmt.text = "₹%.2f".format(maxIn)
        binding.AvgInAmt.text = "₹%.2f".format(avgIn)
    }

    private fun allTransactions() {
        val addT = binding.allTransactions
        addT.setOnClickListener {
            val intent = Intent(this, AllTransactionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addTransaction() {
        val but = binding.addBtn
        but.setOnClickListener {
            val intent = Intent(this, TransactionActivity::class.java)
            startActivity(intent)
        }
    }



}