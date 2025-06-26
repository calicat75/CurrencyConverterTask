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
    private val accountDao: AccountDao,

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

    private val _accounts: MutableStateFlow<List<AccountDbo>> = MutableStateFlow<List<AccountDbo>>(emptyList())
    val accounts: StateFlow<List<AccountDbo>> = _accounts.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val rubAccount = accountDao.getAll().find { it.code == "RUB" }
            if (rubAccount == null) {
                accountDao.insertAll(AccountDbo("RUB", 75_000.0))
            }

            _accounts.value = accountDao.getAll()
        }
        startUpdatingRates()
    }

    fun onCurrencySelected(currencyCode: String) {
        if (_selectedCurrency.value != currencyCode) {
            _selectedCurrency.value = currencyCode
            resetAmount()
            startUpdatingRates()
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
        val selectedCode = selectedCurrency.value
        val amount = inputAmount.value

        if (currency.name == selectedCode) return amount

        val rateFromSelected = rates.value.find { it.currency == selectedCode }?.value
        val rateTo = rates.value.find { it.currency == currency.name }?.value

        return if (rateFromSelected != null && rateTo != null) {
            amount * (rateTo / rateFromSelected)
        } else {
            0.0
        }
    }

    fun getRates() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val ratesList = remoteRatesService.getRates(_selectedCurrency.value, _inputAmount.value)
                _rates.value = ratesList.sortedBy { it.currency }
            } catch (e: Exception) {
                _rates.value = emptyList()
            }
        }
    }

    private fun startUpdatingRates() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                getRates()
                delay(1000)
                startUpdatingRates()
            }
        }
    }

}

