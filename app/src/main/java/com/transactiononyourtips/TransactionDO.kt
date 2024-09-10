package com.transactiononyourtips

import androidx.room.*

@Dao
interface TransactionDO {
    @Query("SELECT * FROM transaction_table WHERE dateOT >= date('now', 'start of month')")
    fun selectData():List<Transactions>

    @Query("SELECT * FROM transaction_table WHERE dateOT >= date('now', 'start of month', '-1 month') AND dateOT < date('now', 'start of month')")
    fun selectPrevData():List<Transactions>

    @Query("SELECT * FROM transaction_table WHERE dateOT >= date('now', 'start of month', '-6 month') AND dateOT <= date('now')")
    fun selectsixMonth():List<Transactions>

    @Query("SELECT * FROM transaction_table WHERE dateOT >= date('now', 'start of month', '-1 year') AND dateOT <= date('now')")
    fun selectPastYear():List<Transactions>

    @Query("SELECT * FROM transaction_table")
    fun selectAllData():List<Transactions>

    @Insert
    fun insertData(vararg transactions: Transactions)

    @Delete
    fun deleteData(transactions: Transactions)

    @Update
    fun updateData(vararg transactions: Transactions)
}
