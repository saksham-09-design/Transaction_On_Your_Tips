package com.transactiononyourtips

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var oldTransactions : List<Transactions>
    private lateinit var deletedTransaction: Transactions

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

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteTransaction(transactions[viewHolder.adapterPosition])
            }
        }
        val swipeHelper = ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(binding.recyclerview)

        addTransaction()
        allTransactions()
    }

    private fun deleteTransaction(transaction: Transactions){
        deletedTransaction = transaction
        oldTransactions = transactions

        GlobalScope.launch {
            dataBase.transactionDO().deleteData(transaction)

            transactions = transactions.filter { it.id != transaction.id }
            runOnUiThread {
                updateDashboard()
                TransactionsAdapt.setData(transactions)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchTransactions()
    }

    private fun fetchTransactions() {
        GlobalScope.launch {
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