package com.example.currencyconverter.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun ExchangeScreen(
    viewModel: ExchangeViewModel,
    navController: NavController
) {
    val fromCurrency by viewModel.fromCurrency.collectAsState()
    val toCurrency by viewModel.toCurrency.collectAsState()
    val fromAmount by viewModel.fromAmount.collectAsState()
    val toAmount by viewModel.toAmount.collectAsState()
    val rate by viewModel.exchangeRate.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Exchange Currency",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "You buy:", fontWeight = FontWeight.SemiBold)
                Text(
                    text = "$toAmount $toCurrency",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "You pay:", fontWeight = FontWeight.SemiBold)
                Text(
                    text = "$fromAmount $fromCurrency",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Exchange Rate:", fontWeight = FontWeight.SemiBold)
                Text(
                    text = "1 $toCurrency = $rate $fromCurrency",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.performExchange(
                    onSuccess = {
                        navController.popBackStack()
                    },
                    onError = { errorMessage ->
                        println("Exchange failed: $errorMessage")
                    }
                )
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Buy")
        }
    }
}
