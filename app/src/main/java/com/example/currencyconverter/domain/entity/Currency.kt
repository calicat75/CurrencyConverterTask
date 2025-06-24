package com.example.currencyconverter.domain.entity

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.painter.Painter
import com.example.currencyconverter.R

enum class Currency {
    USD, GBP, EUR, AUD, BGN, BRL, CAD, CHF, CNY,
    CZK, DKK, HKD, HRK, HUF, IDR, ILS, INR, ISK,
    JPY, KRW, MXN, MYR, NOK, NZD, PHP, PLN, RON,
    RUB, SEK, SGD, THB, TRY, ZAR;

    fun toName(): String = when (this) {
        USD -> "Доллар США"
        GBP -> "Фунт стерлингов"
        EUR -> "Евро"
        AUD -> "Австралийский доллар"
        BGN -> "Болгарский лев"
        BRL -> "Бразильский реал"
        CAD -> "Канадский доллар"
        CHF -> "Швейцарский франк"
        CNY -> "Китайский юань"
        CZK -> "Чешская крона"
        DKK -> "Датская крона"
        HKD -> "Гонконгский доллар"
        HRK -> "Хорватская куна"
        HUF -> "Венгерский форинт"
        IDR -> "Индонезийская рупия"
        ILS -> "Израильский шекель"
        INR -> "Индийская рупия"
        ISK -> "Исландская крона"
        JPY -> "Японская иена"
        KRW -> "Южнокорейская вона"
        MXN -> "Мексиканское песо"
        MYR -> "Малайзийский ринггит"
        NOK -> "Норвежская крона"
        NZD -> "Новозеландский доллар"
        PHP -> "Филиппинское песо"
        PLN -> "Польский злотый"
        RON -> "Румынский лей"
        RUB -> "Российский рубль"
        SEK -> "Шведская крона"
        SGD -> "Сингапурский доллар"
        THB -> "Тайский бат"
        TRY -> "Турецкая лира"
        ZAR -> "Южноафриканский ранд"
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
            TODO("not implemented yet")
        }
    }
}
