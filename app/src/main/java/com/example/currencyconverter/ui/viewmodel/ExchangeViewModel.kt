package com.example.currencyconverter.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.data.dataSource.room.transaction.dao.TransactionDao
import com.example.currencyconverter.data.dataSource.room.transaction.dbo.TransactionDbo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao
) : ViewModel() {

    private val _fromCurrency = MutableStateFlow("")
    val fromCurrency: StateFlow<String> = _fromCurrency.asStateFlow()

    private val _toCurrency = MutableStateFlow("")
    val toCurrency: StateFlow<String> = _toCurrency.asStateFlow()

    private val _fromAmount = MutableStateFlow(0.0)
    val fromAmount: StateFlow<Double> = _fromAmount.asStateFlow()

    private val _toAmount = MutableStateFlow(0.0)
    val toAmount: StateFlow<Double> = _toAmount.asStateFlow()

    private val _exchangeRate = MutableStateFlow(0.0)
    val exchangeRate: StateFlow<Double> = _exchangeRate.asStateFlow()

    fun setExchangeData(
        fromCurrencyCode: String,
        toCurrencyCode: String,
        rate: Double,
        toAmount: Double
    ) {
        _fromCurrency.value = fromCurrencyCode
        _toCurrency.value = toCurrencyCode
        _exchangeRate.value = rate
        _toAmount.value = toAmount
        _fromAmount.value = rate * toAmount
    }

    fun performExchange(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val accounts = accountDao.getAll().associateBy { it.code }.toMutableMap()

            val payerCode = _fromCurrency.value
            val receiverCode = _toCurrency.value
            val amountFrom = _fromAmount.value
            val amountTo = _toAmount.value

            val payerBalance = accounts[payerCode]?.amount ?: 0.0
            val receiverBalance = accounts[receiverCode]?.amount ?: 0.0

            if (payerBalance < amountFrom) {
                onError("Недостаточно средств на счёте $payerCode")
                return@launch
            }

            val updatedPayer = AccountDbo(code = payerCode, amount = payerBalance - amountFrom)
            val updatedReceiver = AccountDbo(code = receiverCode, amount = receiverBalance + amountTo)
            accountDao.insertAll(updatedPayer, updatedReceiver)

            val transaction = TransactionDbo(
                id = 0,
                from = payerCode,
                to = receiverCode,
                fromAmount = amountFrom,
                toAmount = amountTo,
                dateTime = LocalDateTime.now()
            )
            transactionDao.insertAll(transaction)

            onSuccess()
        }
    }
}
