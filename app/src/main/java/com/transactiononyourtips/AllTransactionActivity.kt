package com.transactiononyourtips

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.transactiononyourtips.databinding.ActivityAllTransactionBinding
import kotlin.math.abs

class AllTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllTransactionBinding
    private lateinit var transactions: ArrayList<Transactions>
    private lateinit var transactionsAdapter: TransactionsAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactions = arrayListOf(
            Transactions("Tea",-60.05,"Evening Tea","09/10/2024"),
            Transactions("Coffee",-150.00,"Evening Coffee","09/10/2024"),
            Transactions("Salary",100000.00,"This Month Salary","09/10/2024"),
            Transactions("Lunch",-600.05,"Lunch","09/10/2024")
        )
        transactionsAdapter = TransactionsAdapter(transactions, this)
        linearLayoutManager = LinearLayoutManager(this)

        binding.recyclerview1.apply {
            adapter = transactionsAdapter
            layoutManager = linearLayoutManager
        }
        close()
        updateDashboard()
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

