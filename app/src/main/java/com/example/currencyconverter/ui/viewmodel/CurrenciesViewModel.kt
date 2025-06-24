package com.example.currencyconverter.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    private val remoteRatesService: RemoteRatesServiceImpl,
    private val accountDao: AccountDao
) : ViewModel() {

    private val _selectedCurrency = MutableStateFlow("RUB")
    val selectedCurrency: StateFlow<String> = _selectedCurrency.asStateFlow()

    private val _inputAmount = MutableStateFlow(1.0)
    val inputAmount: StateFlow<Double> = _inputAmount.asStateFlow()

    private val _isInputMode = MutableStateFlow(false)
    val isInputMode: StateFlow<Boolean> = _isInputMode.asStateFlow()

    private val _rates = MutableStateFlow<List<RateDto>>(emptyList())
    val rates: StateFlow<List<RateDto>> = _rates.asStateFlow()

    private var updateJob: Job? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val rubAccount = accountDao.getAll().find { it.code == "RUB" }
            if (rubAccount == null) {
                accountDao.insertAll(AccountDbo("RUB", 75_000.0))
            }
        }
        startUpdatingRates()
    }

    fun onCurrencySelected(currency: String) {
        if (_selectedCurrency.value != currency) {
            _selectedCurrency.value = currency
            _inputAmount.value = 1.0
            _isInputMode.value = false
        }
    }

    fun onAmountChanged(newAmount: Double) {
        _inputAmount.value = newAmount
        _isInputMode.value = true
    }

    fun resetAmount() {
        _inputAmount.value = 1.0
        _isInputMode.value = false
    }

    fun getRates() {
        viewModelScope.launch(Dispatchers.IO) {
            val baseCurrency = _selectedCurrency.value
            val amount = _inputAmount.value

            try {
                val ratesMap = remoteRatesService.getRates(baseCurrency, amount)
                val accounts = accountDao.getAll()
                val accountMap = accounts.associateBy { it.code }

                val filteredRates = ratesMap.mapNotNull { (code, value) ->
                    val balance = accountMap[code]?.amount ?: 0.0

                    if (_isInputMode.value) {
                        val cost = value * amount
                        val payerBalance = accountMap[baseCurrency]?.amount ?: 0.0
                        if (payerBalance < cost) return@mapNotNull null
                    }

                    RateDto(currency = code, value = value)
                }.sortedWith(
                    compareByDescending<RateDto> { it.currency == baseCurrency }
                        .thenBy { it.currency }
                )

                _rates.emit(filteredRates)
            } catch (e: Exception) {
            }
        }
    }

    private fun startUpdatingRates() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                getRates()
                delay(1000)
            }
        }
    }
}

