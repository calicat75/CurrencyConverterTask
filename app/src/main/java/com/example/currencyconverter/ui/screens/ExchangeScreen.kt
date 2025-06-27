package com.example.currencyconverter.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.currencyconverter.ui.viewmodel.ExchangeViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.currencyconverter.ui.viewmodel.CurrenciesViewModel

@Composable
fun ExchangeScreen(
    viewModel: ExchangeViewModel,
    currenciesViewModel: CurrenciesViewModel,
    navController: NavHostController,
    fromCurrencyCode: String,
    toCurrencyCode: String,
    rate: Double,
    toAmount: Double
) {
    LaunchedEffect(Unit) {
        viewModel.initExchange(fromCurrencyCode, toCurrencyCode, rate, toAmount)
    }
    val fromCurrencyCode by viewModel.fromCurrency.collectAsState()
    val toCurrencyCode by viewModel.toCurrency.collectAsState()
    val fromAmount by viewModel.fromAmount.collectAsState()
    val toAmount by viewModel.toAmount.collectAsState()
    val rate by viewModel.exchangeRate.collectAsState()

    val allCurrencies = currenciesViewModel.getCurrenciesForDisplay()
    val accounts by currenciesViewModel.accounts.collectAsState()

    val fromCurrency = allCurrencies.find { it.name == fromCurrencyCode }
    val toCurrency = allCurrencies.find { it.name == toCurrencyCode }


    val fromBalance = accounts.find { it.code == fromCurrencyCode }?.amount ?: 0.0
    val toBalance = accounts.find { it.code == toCurrencyCode }?.amount ?: 0.0


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Exchange",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        if (fromCurrency != null) {
            CurrencyCard(
                currency = fromCurrency,
                isSelected = true,
                inputAmount = fromAmount,
                isInputMode = false,
                onAmountChange = {},
                onClick = {},
                convertedAmount = fromAmount,
                balance = fromBalance,
                onResetAmount = {}
            )
        }

        if (toCurrency != null) {
            CurrencyCard(
                currency = toCurrency,
                isSelected = true,
                inputAmount = toAmount,
                isInputMode = false,
                onAmountChange = {},
                onClick = {},
                convertedAmount = toAmount,
                balance = toBalance,
                onResetAmount = {}
            )
        }

        Text(
            text = "Exchange Rate: 1 $toCurrencyCode = $rate $fromCurrencyCode",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.performExchange(
                    onSuccess = { navController.popBackStack() },
                    onError = { errorMessage -> println("Exchange failed: $errorMessage") }
                )
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Buy")
        }
    }
}

@Composable
fun CurrencyExchangeCard(
    currencyCode: String,
    amount: Double,
    onAmountChange: (Double) -> Unit,
    title: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = title, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = currencyCode,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = if (amount == 0.0) "" else amount.toString(),
                onValueChange = {
                    it.toDoubleOrNull()?.let(onAmountChange)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
