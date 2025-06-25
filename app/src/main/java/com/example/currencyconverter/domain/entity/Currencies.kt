package com.example.currencyconverter.domain.entity

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.painter.Painter
import com.example.currencyconverter.R

enum class Currencies {
    USD, GBP, EUR, RUB, CNY,
//    AUD, BGN, BRL, CAD, CHF,
//    CZK, DKK, HKD, HRK, HUF, IDR, ILS, INR, ISK,
//    JPY, KRW, MXN, MYR, NOK, NZD, PHP, PLN, RON,
//    SEK, SGD, THB, TRY, ZAR
    ;

    fun toName(): String = when (this) {
        USD -> "United States Dollar"
        GBP -> "British Pound Sterling"
        EUR -> "Euro"
        RUB -> "Russian Ruble"
        CNY -> "Chinese Yuan"
// AUD -> "Australian Dollar"
// BGN -> "Bulgarian Lev"
// BRL -> "Brazilian Real"
// CAD -> "Canadian Dollar"
// CHF -> "Swiss Franc"
// CZK -> "Czech Koruna"
// DKK -> "Danish Krone"
// HKD -> "Hong Kong Dollar"
// HRK -> "Croatian Kuna"
// HUF -> "Hungarian Forint"
// IDR -> "Indonesian Rupiah"
// ILS -> "Israeli Shekel"
// INR -> "Indian Rupee"
// ISK -> "Icelandic Krona"
// JPY -> "Japanese Yen"
// KRW -> "South Korean Won"
// MXN -> "Mexican Peso"
// MYR -> "Malaysian Ringgit"
// NOK -> "Norwegian Krone"
// NZD -> "New Zealand Dollar"
// PHP -> "Philippine Peso"
// PLN -> "Polish Zloty"
// RON -> "Romanian Leu"
// SEK -> "Swedish Krona"
// SGD -> "Singapore Dollar"
// THB -> "Thai Baht"
// TRY -> "Turkish Lira"
// ZAR -> "South African Rand"
    }

    @Composable
    fun toPainterResource(): Painter = when (this) {
        USD -> painterResource(id = R.drawable.us_flag)
        GBP -> painterResource(id = R.drawable.uk_flag)
        EUR -> painterResource(id = R.drawable.eu_flag)
        RUB -> painterResource(id = R.drawable.ru_flag)
        CNY -> painterResource(id = R.drawable.cn_flag)
//        AUD -> painterResource(id = R.drawable.aud_flag)
//        BGN -> painterResource(id = R.drawable.bgn_flag)
//        BRL -> painterResource(id = R.drawable.brl_flag)
//        CAD -> painterResource(id = R.drawable.cad_flag)
//        CHF -> painterResource(id = R.drawable.chf_flag)
//        CZK -> painterResource(id = R.drawable.czk_flag)
//        DKK -> painterResource(id = R.drawable.dkk_flag)
//        HKD -> painterResource(id = R.drawable.hkd_flag)
//        HRK -> painterResource(id = R.drawable.hrk_flag)
//        HUF -> painterResource(id = R.drawable.huf_flag)
//        IDR -> painterResource(id = R.drawable.idr_flag)
//        ILS -> painterResource(id = R.drawable.ils_flag)
//        INR -> painterResource(id = R.drawable.inr_flag)
//        ISK -> painterResource(id = R.drawable.isk_flag)
//        JPY -> painterResource(id = R.drawable.jpy_flag)
//        KRW -> painterResource(id = R.drawable.krw_flag)
//        MXN -> painterResource(id = R.drawable.mxn_flag)
//        MYR -> painterResource(id = R.drawable.myr_flag)
//        NOK -> painterResource(id = R.drawable.nok_flag)
//        NZD -> painterResource(id = R.drawable.nzd_flag)
//        PHP -> painterResource(id = R.drawable.php_flag)
//        PLN -> painterResource(id = R.drawable.pln_flag)
//        RON -> painterResource(id = R.drawable.ron_flag)
//        SEK -> painterResource(id = R.drawable.sek_flag)
//        SGD -> painterResource(id = R.drawable.sgd_flag)
//        THB -> painterResource(id = R.drawable.thb_flag)
//        TRY -> painterResource(id = R.drawable.try_flag)
//        ZAR -> painterResource(id = R.drawable.zar_flag)
        else -> {
            TODO("not yet implemented")
        }
    }
}
