package com.transactiononyourtips

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import com.transactiononyourtips.databinding.ActivityTransactionBinding
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
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
    }

    private fun date_inp(){
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(year, monthOfYear, dayOfMonth)
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