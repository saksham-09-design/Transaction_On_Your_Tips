package com.transactiononyourtips

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.transactiononyourtips.databinding.ActivityTransactionBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionBinding
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        calendar = Calendar.getInstance()

        close()
        date_inp()
        add_Transaction()
    }

    private fun InsertTrans(transaction: Transactions) {
        val dataBase = Room.databaseBuilder(this, AppDatabase::class.java, "transaction_table").build()

        GlobalScope.launch {
            dataBase.transactionDO().insertData(transaction)
            finish()
        }
    }

    private fun changeDateFormat(dateInp: String): String {
        val mm = dateInp.substring(0, 2)
        val dd = dateInp.substring(3, 5)
        val yyyy = dateInp.substring(6, 10)
        return "$yyyy-$mm-$dd"
    }

    private fun add_Transaction() {
        val addt = binding.addTransactionBtn
        addt.setOnClickListener {
            val title = binding.titleInput.text.toString()
            val amt = binding.amountInput.text.toString().toDoubleOrNull()
            val desc = binding.descriptionInput.text.toString()
            var dateInp = binding.Date.text.toString()

            if(dateInp == ""){
                dateInp = changeDateFormat(SimpleDateFormat("MM/dd/yyyy").format(Date()))
            } else {
                dateInp = changeDateFormat(dateInp)
            }

            if(title != ""){
                if(amt == null)
                    Toast.makeText(this, "Please Enter Amount", Toast.LENGTH_SHORT).show()
                else {
                    val transaction = Transactions(0,title, amt, desc, dateInp)
                    InsertTrans(transaction)
                }
            } else {
                Toast.makeText(this, "Please Enter Tittle", Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(this,"Transaction Successfully Recorded!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun date_inp(){
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(year, monthOfYear, dayOfMonth)
            val selectedDateInMillis = calendar.timeInMillis
            updateDateInView()
        }
        binding.Date.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun updateDateInView() {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        binding.Date.setText(dateFormat.format(calendar.time))
    }


    private fun close() {
        val but = binding.closeBtn
        but.setOnClickListener {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}