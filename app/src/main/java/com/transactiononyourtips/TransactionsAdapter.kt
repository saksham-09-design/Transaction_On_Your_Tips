package com.transactiononyourtips

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TransactionsAdapter(private var transaction: ArrayList<Transactions>, context: Context):
RecyclerView.Adapter<TransactionsAdapter.TransHolder>() {
    private var colorToApply: Int = ContextCompat.getColor(context, R.color.black)

    class TransHolder(view: View) : RecyclerView.ViewHolder(view){
        val desc : TextView = view.findViewById(R.id.tdec)
        val amt : TextView = view.findViewById(R.id.amt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transactions_layout, parent, false)
        return TransHolder(view)
    }

    override fun onBindViewHolder(holder: TransHolder, position: Int) {
        val transact = transaction[position]
        val context = holder.amt.context

        if(transact.amt >= 0) {
            holder.amt.text = "+₹%.2f".format(transact.amt)
            holder.amt.setTextColor(ContextCompat.getColor(context, R.color.green))
        } else {
            holder.amt.text = "-₹%.2f".format(Math.abs(transact.amt))
            holder.amt.setTextColor(ContextCompat.getColor(context, R.color.red))
        }

        holder.desc.text = transact.t_name
        holder.desc.setTextColor(colorToApply)
    }

    override fun getItemCount(): Int {
        return transaction.size
    }

}