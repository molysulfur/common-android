package com.awonar.android.constrant

val timePeriodsData = arrayListOf(
    "Current quarter" to "currentMonth",
    "Last month" to "1MonthAgo",
    "Last 3 Months" to "3MonthsAgo",
    "Last 6 Months" to "6MonthsAgo",
    "Last 12 Months" to "1YearAgo",
    "Last 2 Year" to "2YearsAgo",
)

val timeStatusData = arrayListOf(
    "popular" to "popular",
    "verified" to "verified"
)

val allocationData = arrayListOf(
    "Currencies" to "currencies",
    "Crypto" to "crypto",
    "Indices" to "indices",
    "Stocks" to "stocks",
    "ETFs" to "etfs",
    "People" to "people",
)


val returnData: ArrayList<Pair<String, List<String?>>> = arrayListOf(
    "Low" to listOf("0", "10"),
    "Medium" to listOf("10", "50"),
    "High" to listOf(null, "50")
)
