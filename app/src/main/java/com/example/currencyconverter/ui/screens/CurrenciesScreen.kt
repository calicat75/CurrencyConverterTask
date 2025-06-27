package com.example.currencyconverter.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.currencyconverter.domain.entity.Currencies
import com.example.currencyconverter.ui.viewmodel.CurrenciesViewModel
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.painterResource
import com.example.currencyconverter.R
import com.example.currencyconverter.ui.viewmodel.ExchangeViewModel

@Composable
fun CurrenciesScreen(
    currenciesViewModel: CurrenciesViewModel,
    exchangeViewModel: ExchangeViewModel,
    navController: NavController
) {
    val selectedCurrencyCode by currenciesViewModel.selectedCurrency.collectAsState()
    val inputAmount by currenciesViewModel.inputAmount.collectAsState()
    val isInputMode by currenciesViewModel.isInputMode.collectAsState()
    val rates by currenciesViewModel.rates.collectAsState()
    val accountList by currenciesViewModel.accounts.collectAsState()

    val availableCurrencies = currenciesViewModel.getCurrenciesForDisplay()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Currency Converter",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        val sortedCurrencies = availableCurrencies.sortedWith(
            compareByDescending<Currencies> { it.name == selectedCurrencyCode }
                .thenBy { it.name }
        )

        sortedCurrencies.forEach { currency ->
            val balance = accountList.find { it.code == currency.name }?.amount ?: 0.0
            CurrencyCard(
                currency = currency,
                isSelected = selectedCurrencyCode == currency.name,
                inputAmount = inputAmount,
                isInputMode = isInputMode,
                onAmountChange = { currenciesViewModel.onAmountChanged(it) },
                onClick = {
                    if (isInputMode && inputAmount > 0) {
                        val rate = rates.find { it.currency == currency.name }?.value ?: 0.0
                        exchangeViewModel.setExchangeData(
                            fromCurrencyCode = selectedCurrencyCode,
                            toCurrencyCode = currency.name,
                            rate = rate,
                            toAmount = inputAmount
                        )
                        navController.navigate(
                            "exchange?fromCurrency=$selectedCurrencyCode&toCurrency=${currency.name}&rate=$rate&toAmount=$inputAmount"
                        )
                    } else {
                        currenciesViewModel.onCurrencySelected(currency.name)
                    }
                },
                convertedAmount = rates.find { it.currency == currency.name }?.value ?: 0.0,
                balance = balance,
                onResetAmount = { currenciesViewModel.resetAmount() }
            )
        }
    }
}

@Composable
fun CurrencyCard(
    currency: Currencies,
    isSelected: Boolean,
    inputAmount: Double,
    isInputMode: Boolean,
    onAmountChange: (Double) -> Unit,
    onClick: () -> Unit,
    convertedAmount: Double,
    balance: Double,
    onResetAmount: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = currency.toPainterResource(),
                contentDescription = "${currency.name} flag",
                modifier = Modifier
                    .size(width = 40.dp, height = 24.dp)
                    .padding(end = 12.dp),
                contentScale = ContentScale.FillBounds
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = currency.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = currency.toName(),
                    fontSize = 14.sp,
                    color = Color(0xFF888888)
                )
                if (balance > 0.0) {
                    Text(
                        text = "Balance: %.2f".format(balance),
                        fontSize = 14.sp,
                        color = Color(0xFF888888)
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                if (isSelected) {
                    if (isInputMode) {
                        OutlinedTextField(
                            value = inputAmount.toString(),
                            onValueChange = { value ->
                                value.toDoubleOrNull()?.let(onAmountChange)
                            },
                            modifier = Modifier.width(100.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = LocalTextStyle.current.copy(color = Color.Black)
                            )
                        Image(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "Clear amount",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { onResetAmount() }
                        )
                    } else {
                        Text(
                            text = "${currency.symbol()}${"%.5f".format(inputAmount)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .clickable { onAmountChange(inputAmount) }
                        )
                    }
                } else {
                    Text(
                        text = "${currency.symbol()}${"%.5f".format(convertedAmount)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}