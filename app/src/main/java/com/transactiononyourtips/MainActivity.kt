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
    private lateinit var adapter: TransactionsAdapter
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
        sharedPrefOut()
        darkMode()
        allTransactions()
    }

    private fun sharedPrefInp(mode1: Int){
        val sharedPreference = getSharedPreferences("Mode", MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putInt("Modedark",mode1)
        editor.apply()
    }

    private fun sharedPrefOut(): Int{
        val sharedPreference = getSharedPreferences("Mode", MODE_PRIVATE)
        val mod = sharedPreference.getInt("Modedark",0)
        return mod
    }

    private fun darkMode() {
        val db = binding.darkMode
        adapter = TransactionsAdapter(transactions, this)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
        val a = sharedPrefOut()
        if (a == 0) {
            binding.root.setBackgroundColor(getResources().getColor(R.color.dgrey))
            binding.dashboard.setBackgroundColor(getResources().getColor(R.color.dgrey_b))
            binding.abal.setTextColor(getResources().getColor(R.color.white))
            binding.income1.setTextColor(getResources().getColor(R.color.white))
            binding.exp1.setTextColor(getResources().getColor(R.color.white))
            binding.recyclerviewTitle.setTextColor(getResources().getColor(R.color.white))
            binding.recyclerview.setBackgroundColor(getResources().getColor(R.color.dgrey))
            binding.MinIn.setTextColor(getResources().getColor(R.color.white))
            binding.MinSp.setTextColor(getResources().getColor(R.color.white))
            binding.MaxIn.setTextColor(getResources().getColor(R.color.white))
            binding.MaxSp.setTextColor(getResources().getColor(R.color.white))
            binding.AvgIn.setTextColor(getResources().getColor(R.color.white))
            binding.AvgSp.setTextColor(getResources().getColor(R.color.white))


            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
            val newColor = ContextCompat.getColor(this, R.color.white)
            adapter.changeColorForAllItems(newColor)
            sharedPrefInp(0)
        } else {
            binding.root.setBackgroundColor(getResources().getColor(R.color.white))
            binding.dashboard.setBackgroundColor(getResources().getColor(R.color.white))
            binding.abal.setTextColor(getResources().getColor(R.color.black))
            binding.income1.setTextColor(getResources().getColor(R.color.black))
            binding.exp1.setTextColor(getResources().getColor(R.color.black))
            binding.recyclerviewTitle.setTextColor(getResources().getColor(R.color.black))
            binding.recyclerview.setBackgroundColor(getResources().getColor(R.color.white))
            binding.MinIn.setTextColor(getResources().getColor(R.color.black))
            binding.MinSp.setTextColor(getResources().getColor(R.color.black))
            binding.MaxIn.setTextColor(getResources().getColor(R.color.black))
            binding.MaxSp.setTextColor(getResources().getColor(R.color.black))
            binding.AvgIn.setTextColor(getResources().getColor(R.color.black))
            binding.AvgSp.setTextColor(getResources().getColor(R.color.black))

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
            val newColor = ContextCompat.getColor(this, R.color.black)
            adapter.changeColorForAllItems(newColor)
            sharedPrefInp(1)
        }

        db.setOnClickListener {

            val a1 = sharedPrefOut()
            if (a1 == 1) {
                binding.root.setBackgroundColor(getResources().getColor(R.color.dgrey))
                binding.dashboard.setBackgroundColor(getResources().getColor(R.color.dgrey_b))
                binding.abal.setTextColor(getResources().getColor(R.color.white))
                binding.income1.setTextColor(getResources().getColor(R.color.white))
                binding.exp1.setTextColor(getResources().getColor(R.color.white))
                binding.recyclerviewTitle.setTextColor(getResources().getColor(R.color.white))
                binding.recyclerview.setBackgroundColor(getResources().getColor(R.color.dgrey))
                binding.MinIn.setTextColor(getResources().getColor(R.color.white))
                binding.MinSp.setTextColor(getResources().getColor(R.color.white))
                binding.MaxIn.setTextColor(getResources().getColor(R.color.white))
                binding.MaxSp.setTextColor(getResources().getColor(R.color.white))
                binding.AvgIn.setTextColor(getResources().getColor(R.color.white))
                binding.AvgSp.setTextColor(getResources().getColor(R.color.white))

                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = adapter
                val newColor1 = ContextCompat.getColor(this, R.color.white)
                adapter.changeColorForAllItems(newColor1)

                sharedPrefInp(0)
            } else {
                binding.root.setBackgroundColor(getResources().getColor(R.color.white))
                binding.dashboard.setBackgroundColor(getResources().getColor(R.color.white))
                binding.abal.setTextColor(getResources().getColor(R.color.black))
                binding.income1.setTextColor(getResources().getColor(R.color.black))
                binding.exp1.setTextColor(getResources().getColor(R.color.black))
                binding.recyclerviewTitle.setTextColor(getResources().getColor(R.color.black))
                binding.recyclerview.setBackgroundColor(getResources().getColor(R.color.white))
                binding.MinIn.setTextColor(getResources().getColor(R.color.black))
                binding.MinSp.setTextColor(getResources().getColor(R.color.black))
                binding.MaxIn.setTextColor(getResources().getColor(R.color.black))
                binding.MaxSp.setTextColor(getResources().getColor(R.color.black))
                binding.AvgIn.setTextColor(getResources().getColor(R.color.black))
                binding.AvgSp.setTextColor(getResources().getColor(R.color.black))

                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = adapter
                val newColor1 = ContextCompat.getColor(this, R.color.black)
                adapter.changeColorForAllItems(newColor1)
                sharedPrefInp(1)
            }
        }
    }

    private fun undoDelete(){
        GlobalScope.launch {
            dataBase.transactionDO().insertData(deletedTransaction)
            transactions = oldTransactions

            runOnUiThread {
                TransactionsAdapt.setData(transactions)
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
                TransactionsAdapt.setData(transactions)
                showSnackbar()
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