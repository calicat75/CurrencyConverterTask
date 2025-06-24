package com.example.currencyconverter.ui.nav

import androidx.annotation.DrawableRes
import com.example.currencyconverter.R

sealed class Screen(val route: String, val title: String, @DrawableRes val icon: Int) {
    data object Currencies : Screen("currencies", "Валюты", R.drawable.ic_currencies)
    data object Exchange : Screen("exchange/{from}/{to}/{amount}", "Обмен", R.drawable.ic_exchange)
    data object Transactions : Screen("transactions", "Транзакции", R.drawable.ic_transactions)

    companion object {
        val entries = listOf(
            Currencies,
            Transactions,
        )
    }

    fun exchangeRoute(from: String, to: String, amount: Double): String {
        return "exchange/$from/$to/$amount"
    }
}
