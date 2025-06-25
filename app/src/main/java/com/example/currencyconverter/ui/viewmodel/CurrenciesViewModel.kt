package com.example.currencyconverter.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.dataSource.remote.RatesService
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.domain.entity.Currencies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    private val remoteRatesService: RatesService,
    private val accountDao: AccountDao
) : ViewModel() {

    private var updateJob: Job? = null

    private val _selectedCurrency = MutableStateFlow("RUB")
    val selectedCurrency: StateFlow<String> = _selectedCurrency.asStateFlow()

    private val _inputAmount = MutableStateFlow(1.0)
    val inputAmount: StateFlow<Double> = _inputAmount.asStateFlow()

    private val _isInputMode = MutableStateFlow(false)
    val isInputMode: StateFlow<Boolean> = _isInputMode.asStateFlow()

    private val _rates = MutableStateFlow<List<RateDto>>(emptyList())
    val rates: StateFlow<List<RateDto>> = _rates.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val rubAccount = accountDao.getAll().find { it.code == "RUB" }
            if (rubAccount == null) {
                accountDao.insertAll(AccountDbo("RUB", 75_000.0))
            }
        }
        startUpdatingRates()
    }

    fun onCurrencySelected(currencyCode: String) {
        if (_selectedCurrency.value != currencyCode) {
            _selectedCurrency.value = currencyCode
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

    fun getAvailableCurrencies(): List<Currencies> = Currencies.values().toList()

    fun onCurrencyClicked(currency: Currencies, onNavigate: () -> Unit) {
        onCurrencySelected(currency.name)
        onNavigate()
    }

    fun getConvertedAmount(currency: Currencies): Double {
        val currentRates = rates.value
        return currentRates.find { it.currency == currency.name }?.value ?: 0.0
    }

    fun getRates() {
        viewModelScope.launch(Dispatchers.IO) {
            val baseCurrency = _selectedCurrency.value
            val amount = _inputAmount.value

            try {
                val ratesList: List<RateDto> = remoteRatesService.getRates(baseCurrency, amount)
                val accounts = accountDao.getAll()
                val accountMap: Map<String, AccountDbo> = accounts.associateBy { it.code }

                val filteredRates = ratesList.mapNotNull { rateDto ->
                    val balance = accountMap[rateDto.currency]?.amount ?: 0.0

                    if (_isInputMode.value) {
                        val cost = rateDto.value * amount
                        val payerBalance = accountMap[baseCurrency]?.amount ?: 0.0
                        if (payerBalance < cost) return@mapNotNull null
                    }

                    RateDto(currency = rateDto.currency, value = rateDto.value)
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

