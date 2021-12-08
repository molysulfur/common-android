package com.awonar.android.constrant

enum class MarketOrderType {
    ENTRY_ORDER, // market close open at market
    PENDING_ORDER, // close at price
    OPEN_ORDER // market open
}