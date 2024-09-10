package com.transactiononyourtips

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "transaction_table")
data class Transactions(
    @PrimaryKey(autoGenerate = true)val id: Int,
    val t_name: String,
    val amt: Double,
    val desc: String,
    val dateOT: String
): Serializable{

}