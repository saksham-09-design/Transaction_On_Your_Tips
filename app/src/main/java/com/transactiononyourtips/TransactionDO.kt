package com.transactiononyourtips

import androidx.room.*

@Dao
interface TransactionDO {
    @Query("SELECT * FROM transaction_table WHERE dateOT >= date('now', 'start of month')")
    fun selectData():List<Transactions>

    @Query("SELECT * FROM transaction_table")
    fun selectAllData():List<Transactions>

    @Insert
    fun insertData(vararg transactions: Transactions)

    @Delete
    fun deleteData(transactions: Transactions)

    @Update
    fun updateData(vararg transactions: Transactions)
}
