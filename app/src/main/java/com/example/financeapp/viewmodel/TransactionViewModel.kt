package com.example.financeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.financeapp.model.Transaction
import com.example.financeapp.model.TransactionType
import com.example.financeapp.repository.TransactionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions

    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome

    private val _totalExpenses = MutableStateFlow(0.0)
    val totalExpenses: StateFlow<Double> = _totalExpenses

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    init {
        loadTransactions()
        calculateTotals()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            try {
                repository.getAllTransactions()
                    .catch { e ->
                        _error.emit("Erro ao carregar transações: ${e.message}")
                    }
                    .collect { transactions ->
                        _transactions.value = transactions
                    }
            } catch (e: Exception) {
                _error.emit("Erro inesperado: ${e.message}")
            }
        }
    }

    private fun calculateTotals() {
        viewModelScope.launch {
            try {
                combine(
                    repository.getTotalByType(TransactionType.INCOME),
                    repository.getTotalByType(TransactionType.EXPENSE)
                ) { income, expense ->
                    _totalIncome.value = income ?: 0.0
                    _totalExpenses.value = expense ?: 0.0
                }
                    .catch { e ->
                        _error.emit("Erro ao calcular totais: ${e.message}")
                    }
                    .collect()
            } catch (e: Exception) {
                _error.emit("Erro inesperado: ${e.message}")
            }
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.insertTransaction(transaction)
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.updateTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    fun getFilteredTransactions(type: TransactionType?, startDate: Date, endDate: Date) {
        viewModelScope.launch {
            if (type != null) {
                repository.getTransactionsByTypeAndDateRange(type, startDate, endDate)
                    .collect { filtered ->
                        _transactions.value = filtered
                    }
            } else {
                loadTransactions()
            }
        }
    }

    fun sortTransactionsByDate() {
        viewModelScope.launch {
            _transactions.value = _transactions.value.sortedBy { it.date }
        }
    }

    fun sortTransactionsByAmount() {
        viewModelScope.launch {
            _transactions.value = _transactions.value.sortedBy { it.amount }
        }
    }

    class Factory(private val repository: TransactionRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TransactionViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
