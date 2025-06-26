package com.example.financeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.financeapp.databinding.ItemTransactionBinding
import com.example.financeapp.model.Transaction
import com.example.financeapp.model.TransactionType
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionAdapter(
    private val onItemClick: (Transaction) -> Unit,
    private val onItemLongClick: (Transaction) -> Unit,
    private val onDeleteClick: (Transaction) -> Unit // Adicionado para lidar com exclusão
) : ListAdapter<Transaction, TransactionAdapter.ViewHolder>(TransactionDiffCallback()) {

    class ViewHolder(
        private val binding: ItemTransactionBinding,
        private val onItemClick: (Transaction) -> Unit,
        private val onItemLongClick: (Transaction) -> Unit,
        private val onDeleteClick: (Transaction) -> Unit // Adicionado para lidar com exclusão
    ) : RecyclerView.ViewHolder(binding.root) {

        private val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

        fun bind(transaction: Transaction) {
            binding.apply {
                descriptionText.text = transaction.description
                categoryText.text = transaction.category
                dateText.text = dateFormat.format(transaction.date)

                val amount = numberFormat.format(transaction.amount)
                amountText.text = amount
                amountText.setTextColor(
                    root.context.getColor(
                        if (transaction.type == TransactionType.INCOME)
                            android.R.color.holo_green_dark
                        else
                            android.R.color.holo_red_dark
                    )
                )

                root.setOnClickListener { onItemClick(transaction) }
                root.setOnLongClickListener {
                    onItemLongClick(transaction)
                    true
                }
                deleteButton.setOnClickListener { onDeleteClick(transaction) } // Clique no botão de exclusão
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onItemClick, onItemLongClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }
}
