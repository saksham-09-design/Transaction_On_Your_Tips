package com.transactiononyourtips

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.transactiononyourtips.databinding.ActivityAllTransactionBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.abs

class AllTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllTransactionBinding
    private lateinit var transactions: List<Transactions>
    private lateinit var transactionsAdapter: TransactionsAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var dataBase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactions = arrayListOf()

        dataBase = Room.databaseBuilder(this, AppDatabase::class.java, "transaction_table").build()

        transactionsAdapter = TransactionsAdapter(transactions, this)
        linearLayoutManager = LinearLayoutManager(this)

        binding.recyclerview1.apply {
            adapter = transactionsAdapter
            layoutManager = linearLayoutManager
        }

        close()
        updateDashboard()
        all()
        pastMonth()
        pastYear()
        pastSixMonth()
    }

    private fun pastMonth(){
        val pm = binding.pastMonth
        pm.setOnClickListener {
            transactions = arrayListOf()
            GlobalScope.launch {
                transactions = dataBase.transactionDO().selectPrevData()
                runOnUiThread {
                    transactionsAdapter.setData(transactions)
                    updateDashboard()
                }
            }
        }
    }

    private fun pastSixMonth(){
        val psm = binding.sixMonths
        psm.setOnClickListener {
            transactions = arrayListOf()
            GlobalScope.launch {
                transactions = dataBase.transactionDO().selectsixMonth()
                runOnUiThread {
                    transactionsAdapter.setData(transactions)
                    updateDashboard()
                }
            }
        }
    }

    private fun all(){
        val al = binding.allTrans
        al.setOnClickListener {
            transactions = arrayListOf()
            GlobalScope.launch {
                transactions = dataBase.transactionDO().selectAllData()
                runOnUiThread {
                    transactionsAdapter.setData(transactions)
                    updateDashboard()
                }
            }
        }
    }

    private fun pastYear(){
        val py = binding.oneYear
        py.setOnClickListener {
            transactions = arrayListOf()
            GlobalScope.launch {
                transactions = dataBase.transactionDO().selectPastYear()
                runOnUiThread {
                    transactionsAdapter.setData(transactions)
                    updateDashboard()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchTransactions()
    }

    private fun fetchTransactions() {
        GlobalScope.launch {
            transactions = dataBase.transactionDO().selectAllData()

            runOnUiThread {
                transactionsAdapter.setData(transactions)
                updateDashboard()
            }
        }
    }

    private fun updateDashboard() {
        val minSp = transactions.filter{it.amt < 0}.map{it.amt}.maxOrNull()?:0.0
        val maxSp = transactions.filter{it.amt < 0}.map{it.amt}.minOrNull()?:0.0
        val avgSp = transactions.filter{it.amt < 0}.map{it.amt}.average()
        val maxIn = transactions.filter{it.amt > 0}.map{it.amt}.maxOrNull()?:0.0
        val minIn = transactions.filter{it.amt > 0}.map{it.amt}.minOrNull()?:0.0
        val avgIn = transactions.filter{it.amt > 0}.map{it.amt}.average()

        binding.MinSpAmt.text = "-₹%.2f".format(abs(minSp))
        binding.MaxSpAmt.text = "-₹%.2f".format(abs(maxSp))
        binding.AvgSpAmt.text = "-₹%.2f".format(abs(avgSp))
        binding.MinInAmt.text = "₹%.2f".format(minIn)
        binding.MaxInAmt.text = "₹%.2f".format(maxIn)
        binding.AvgInAmt.text = "₹%.2f".format(avgIn)
    }

    private fun close() {
        val but = binding.closeBtn
        but.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}

