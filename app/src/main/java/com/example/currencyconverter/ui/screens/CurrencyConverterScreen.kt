package com.example.currencyconverter.ui.screens

import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import com.example.currencyconverter.R
import com.example.currencyconverter.ui.components.CurrencyItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun CurrencyConverterScreen() {
    val currencies = listOf(
        CurrencyItem("USD", "US Dollar", "$1.44", "$1.0000", R.drawable.us_flag),
        CurrencyItem("EUR", "Euro", "€2.50", "€0.9200", R.drawable.eu_flag),
        CurrencyItem("GBP", "British Pound", "£3.10", "£0.8100", R.drawable.uk_flag),
        CurrencyItem("RUB", "Russian Ruble", "₽120.00", "₽89.5000", R.drawable.ru_flag),
        CurrencyItem("CNY", "Chinese Yuan", "¥18.90", "¥7.2000", R.drawable.cn_flag)
    )

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

        currencies.forEach { currency ->
            CurrencyCard(currency)
        }
    }
}

@Composable
fun CurrencyCard(item: CurrencyItem) {
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
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.flagResId),
                contentDescription = "${item.code} flag",
                modifier = Modifier
                    .size(width = 40.dp, height = 24.dp)
                    .padding(end = 12.dp),
                contentScale = ContentScale.FillBounds
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.code,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = item.name,
                    fontSize = 14.sp,
                    color = Color(0xFF888888)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = item.balance,
                    fontSize = 14.sp,
                    color = Color(0xFF888888)
                )
                Text(
                    text = item.rate,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
