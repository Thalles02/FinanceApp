package com.example.financeapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val description: String,
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val date: Date
)

enum class TransactionType {
    INCOME, EXPENSE
}
