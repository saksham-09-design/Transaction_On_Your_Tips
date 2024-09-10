package com.transactiononyourtips

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Transactions::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDO() : TransactionDO
}