package com.example.financeapp.repository

import com.example.financeapp.data.TransactionDao
import com.example.financeapp.model.Transaction
import com.example.financeapp.model.TransactionType
import kotlinx.coroutines.flow.Flow
import java.util.Date

class TransactionRepository(private val transactionDao: TransactionDao) {
    fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAllTransactions()

    fun getTransactionsByTypeAndDateRange(
        type: TransactionType,
        startDate: Date,
        endDate: Date
    ): Flow<List<Transaction>> = transactionDao.getTransactionsByTypeAndDateRange(type, startDate, endDate)

    fun getTotalByType(type: TransactionType): Flow<Double?> = transactionDao.getTotalByType(type)

    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }
}
