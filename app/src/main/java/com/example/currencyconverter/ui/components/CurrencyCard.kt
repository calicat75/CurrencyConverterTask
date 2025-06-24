package com.example.currencyconverter.ui.components

data class CurrencyItem(
    val code: String,
    val name: String,
    val balance: String,
    val rate: String,
    val flagResId: Int
)