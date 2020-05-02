package com.example.finalproject.db

import java.util.*

enum class CurrencyTypes(val currencyCode: String, val locale: Locale) {
    USD("USD", Locale.US),
    EUR("EUR", Locale.FRANCE),
    GBP("GBP", Locale.UK),
    JPY("JPY", Locale.JAPAN)
}