package com.example.financeapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.financeapp.data.model.Transaction

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAll(): LiveData<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)
}
