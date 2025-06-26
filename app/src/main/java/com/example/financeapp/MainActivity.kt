package com.example.financeapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financeapp.data.AppDatabase
import com.example.financeapp.databinding.ActivityMainBinding
import com.example.financeapp.databinding.DialogTransactionBinding
import com.example.financeapp.model.Transaction
import com.example.financeapp.model.TransactionType
import com.example.financeapp.repository.TransactionRepository
import com.example.financeapp.ui.TransactionAdapter
import com.example.financeapp.viewmodel.TransactionViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TransactionAdapter
    private val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    private val viewModel: TransactionViewModel by viewModels {
        val database = AppDatabase.getDatabase(this)
        val repository = TransactionRepository(database.transactionDao())
        TransactionViewModel.Factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupRecyclerView()
        setupFab()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = TransactionAdapter(
            onItemClick = { transaction -> showTransactionDialog(transaction) },
            onItemLongClick = { transaction -> showDeleteDialog(transaction) },
            onDeleteClick = { transaction -> showDeleteDialog(transaction) } // Adicionado para lidar com exclusão
        )
        binding.transactionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupFab() {
        binding.addTransactionFab.setOnClickListener {
            showTransactionDialog(null)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.transactions.collect { transactions ->
                        adapter.submitList(transactions)
                    }
                }
                launch {
                    viewModel.totalIncome.collect { income ->
                        binding.totalIncomeText.text = numberFormat.format(income)
                    }
                }
                launch {
                    viewModel.totalExpenses.collect { expenses ->
                        binding.totalExpensesText.text = numberFormat.format(expenses)
                    }
                }
            }
        }
    }

    private fun showTransactionDialog(transaction: Transaction?) {
        val dialogBinding = DialogTransactionBinding.inflate(layoutInflater)
        val isEdit = transaction != null

        if (isEdit) {
            dialogBinding.apply {
                descriptionInput.setText(transaction!!.description)
                amountInput.setText(transaction.amount.toString())
                categoryInput.setText(transaction.category)
                if (transaction.type == TransactionType.INCOME) {
                    incomeRadio.isChecked = true
                } else {
                    expenseRadio.isChecked = true
                }
            }
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(if (isEdit) "Editar Transação" else "Nova Transação")
            .setView(dialogBinding.root)
            .setPositiveButton("Salvar") { _, _ ->
                val description = dialogBinding.descriptionInput.text.toString()
                val amount = dialogBinding.amountInput.text.toString().toDoubleOrNull()
                val category = dialogBinding.categoryInput.text.toString()
                val type = if (dialogBinding.incomeRadio.isChecked)
                    TransactionType.INCOME else TransactionType.EXPENSE

                if (description.isNotBlank() && amount != null && category.isNotBlank()) {
                    if (isEdit) {
                        viewModel.updateTransaction(
                            transaction!!.copy(
                                description = description,
                                amount = amount,
                                category = category,
                                type = type
                            )
                        )
                        Toast.makeText(this, "Transação atualizada", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.addTransaction(
                            Transaction(
                                description = description,
                                amount = amount,
                                category = category,
                                type = type,
                                date = Date()
                            )
                        )
                        Toast.makeText(this, "Transação adicionada", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showDeleteDialog(transaction: Transaction) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Excluir Transação")
            .setMessage("Tem certeza que deseja excluir esta transação?")
            .setPositiveButton("Excluir") { _, _ ->
                viewModel.deleteTransaction(transaction)
                Toast.makeText(this, "Transação excluída", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_filter_all -> {
                viewModel.getFilteredTransactions(null, Date(0), Date())
                true
            }
            R.id.menu_filter_income -> {
                viewModel.getFilteredTransactions(TransactionType.INCOME, Date(0), Date())
                true
            }
            R.id.menu_filter_expense -> {
                viewModel.getFilteredTransactions(TransactionType.EXPENSE, Date(0), Date())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}