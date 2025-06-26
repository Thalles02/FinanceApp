package com.example.financeapp.data.repository

import androidx.lifecycle.LiveData
import com.example.financeapp.data.dao.TransactionDao
import com.example.financeapp.data.model.Transaction

class TransactionRepository(private val dao: TransactionDao) {
    val allTransactions: LiveData<List<Transaction>> = dao.getAll()

    suspend fun insert(transaction: Transaction) = dao.insert(transaction)
    suspend fun update(transaction: Transaction) = dao.update(transaction)
    suspend fun delete(transaction: Transaction) = dao.delete(transaction)
}
