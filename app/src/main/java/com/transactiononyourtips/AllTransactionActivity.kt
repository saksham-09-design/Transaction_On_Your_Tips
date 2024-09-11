package com.transactiononyourtips

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
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
    private lateinit var oldTransactions : List<Transactions>
    private lateinit var deletedTransaction: Transactions

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
        swipeHelper.attachToRecyclerView(binding.recyclerview1)

        close()
        updateDashboard()
        all()
        pastMonth()
        pastYear()
        pastSixMonth()
        darkMode()
    }

    private fun darkMode() {
        val sharedPrefrences = getSharedPreferences("Mode", MODE_PRIVATE)
        val a = sharedPrefrences.getInt("Modedark", 1)

        if (a == 0) {
            binding.root.setBackgroundColor(getResources().getColor(R.color.dgrey_b))
            binding.recyclerviewTitle.setTextColor(getResources().getColor(R.color.white))
            binding.MinIn.setTextColor(getResources().getColor(R.color.white))
            binding.MinSp.setTextColor(getResources().getColor(R.color.white))
            binding.MaxIn.setTextColor(getResources().getColor(R.color.white))
            binding.closeBtn.setImageResource(R.drawable.close_w)
            binding.MaxSp.setTextColor(getResources().getColor(R.color.white))
            binding.AvgIn.setTextColor(getResources().getColor(R.color.white))
            binding.AvgSp.setTextColor(getResources().getColor(R.color.white))
            val newColor = ContextCompat.getColor(this, R.color.white)
            transactionsAdapter.changeColorForAllItems(newColor)
        } else {
            binding.root.setBackgroundColor(getResources().getColor(R.color.white))
            binding.recyclerviewTitle.setTextColor(getResources().getColor(R.color.black))
            binding.MinIn.setTextColor(getResources().getColor(R.color.black))
            binding.MinSp.setTextColor(getResources().getColor(R.color.black))
            binding.MaxIn.setTextColor(getResources().getColor(R.color.black))
            binding.closeBtn.setImageResource(R.drawable.close)
            binding.MaxSp.setTextColor(getResources().getColor(R.color.black))
            binding.AvgIn.setTextColor(getResources().getColor(R.color.black))
            binding.AvgSp.setTextColor(getResources().getColor(R.color.black))
            val newColor = ContextCompat.getColor(this, R.color.black)
            transactionsAdapter.changeColorForAllItems(newColor)
        }
    }

    private fun undoDelete(){
        GlobalScope.launch {
            dataBase.transactionDO().insertData(deletedTransaction)
            transactions = oldTransactions

            runOnUiThread {
                transactionsAdapter.setData(transactions)
                updateDashboard()
            }
        }
    }

    private fun showSnackbar(){
        val view = binding.coordinator
        val snackbar = Snackbar.make(view, "Transaction Deleted!", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo"){
            undoDelete()
        }
            .setActionTextColor(ContextCompat.getColor(this, R.color.green))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .setBackgroundTint(ContextCompat.getColor(this, R.color.black))
            .show()
    }

    private fun deleteTransaction(transaction: Transactions){
        deletedTransaction = transaction
        oldTransactions = transactions

        GlobalScope.launch {
            dataBase.transactionDO().deleteData(transaction)

            transactions = transactions.filter { it.id != transaction.id }
            runOnUiThread {
                updateDashboard()
                transactionsAdapter.setData(transactions)
                showSnackbar()
            }
        }
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

