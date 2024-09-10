package com.transactiononyourtips

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.transactiononyourtips.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var TransactionsAdapt: TransactionsAdapter
    private lateinit var transactions: List<Transactions>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var dataBase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        transactions = arrayListOf()
        dataBase = Room.databaseBuilder(this, AppDatabase::class.java, "transaction_table").build()

        TransactionsAdapt = TransactionsAdapter(transactions, this)
        linearLayoutManager = LinearLayoutManager(this)

        binding.recyclerview.apply {
            adapter = TransactionsAdapt
            layoutManager = linearLayoutManager
        }
        addTransaction()
        allTransactions()
    }

    override fun onResume() {
        super.onResume()
        fetchTransactions()
    }

    private fun fetchTransactions() {
        GlobalScope.launch {
            dataBase.transactionDO().insertData(Transactions(0,"Tea",-60.0,"Evening Tea","2024-09-10"))
            transactions = dataBase.transactionDO().selectData()

            runOnUiThread {
                updateDashboard()
                TransactionsAdapt.setData(transactions)
            }
        }
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