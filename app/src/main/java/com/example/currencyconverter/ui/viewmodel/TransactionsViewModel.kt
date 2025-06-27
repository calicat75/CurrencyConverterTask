package com.example.currencyconverter.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.dataSource.room.transaction.dao.TransactionDao
import com.example.currencyconverter.data.dataSource.room.transaction.dbo.TransactionDbo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionDao: TransactionDao
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<TransactionDbo>>(emptyList())
    val transactions: StateFlow<List<TransactionDbo>> get() = _transactions

    init {
        loadTransactions()
    }

    fun loadTransactions() {
        viewModelScope.launch(Dispatchers.IO) {
            val txList = transactionDao.getAll()
            _transactions.emit(txList)
        }
    }
}

