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

@Composable
fun CurrenciesScreen(
    currenciesViewModel: CurrenciesViewModel,
    navController: NavController
) {
    val selectedCurrencyCode by currenciesViewModel.selectedCurrency.collectAsState()
    val inputAmount by currenciesViewModel.inputAmount.collectAsState()
    val isInputMode by currenciesViewModel.isInputMode.collectAsState()
    val availableCurrencies = currenciesViewModel.getAvailableCurrencies()

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

        availableCurrencies.forEach { currency ->
            CurrencyCard(
                currency = currency,
                isSelected = selectedCurrencyCode == currency.name,
                inputAmount = inputAmount,
                isInputMode = isInputMode,
                onAmountChange = { currenciesViewModel.onAmountChanged(it) },
                onClick = {
                    currenciesViewModel.onCurrencyClicked(currency) {
                        navController.navigate("exchange/${currency.name}")
                    }
                },
                convertedAmount = currenciesViewModel.getConvertedAmount(currency)
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
    convertedAmount: Double
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
                Text(
                    text = "Balance: 100",
                    fontSize = 14.sp,
                    color = Color(0xFF888888)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                if (isSelected && isInputMode) {
                    OutlinedTextField(
                        value = inputAmount.toString(),
                        onValueChange = { value ->
                            value.toDoubleOrNull()?.let(onAmountChange)
                        },
                        modifier = Modifier.width(100.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                } else {
                    Text(
                        text = String.format("%.2f", convertedAmount),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}