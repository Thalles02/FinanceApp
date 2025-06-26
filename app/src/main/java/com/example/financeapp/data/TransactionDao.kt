package com.example.financeapp.data

import androidx.room.*
import com.example.financeapp.model.Transaction
import com.example.financeapp.model.TransactionType
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE type = :type AND date BETWEEN :startDate AND :endDate")
    fun getTransactionsByTypeAndDateRange(type: TransactionType, startDate: Date, endDate: Date): Flow<List<Transaction>>

    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type")
    fun getTotalByType(type: TransactionType): Flow<Double?>
}
