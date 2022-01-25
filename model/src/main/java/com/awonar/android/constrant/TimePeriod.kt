package com.awonar.android.constrant

val selectData = mutableMapOf(
    "period" to arrayListOf(
        "Current quarter" to "currentMonth",
        "Last month" to "1MonthAgo",
        "Last 3 Months" to "3MonthsAgo",
        "Last 6 Months" to "6MonthsAgo",
        "Last 12 Months" to "1YearAgo",
        "Last 2 Year" to "2YearsAgo",
    ),
    "status" to arrayListOf(
        "popular" to "popular",
        "verified" to "verified"
    ),
    "allocation" to arrayListOf(
        "Currencies" to "currencies",
        "Crypto" to "crypto",
        "Indices" to "indices",
        "Stocks" to "stocks",
        "ETFs" to "etfs",
        "People" to "people",
    )
)

val inputDescriptionData = mutableMapOf(
    "return" to "Discover people based on their trading performance.",
    "risk" to "Risk score is calculated or each user from 1 to 10, where 1 is the lowest possible risk, and 10 is the highest possible risk.",
    "numberCopy" to "Discover people based on their social activity and stats.",
    "active" to "An active user is one that has at least q1 open position.",
    "profitable" to "Discover people based on their trading performance.",
    "numberTrades" to "Discover people based on their trading activity and volume levels.",
    "daily" to "Drawdown is a measurement of decline from an asset/user’s highest point to its lowest point, in a given time.",
    "weekly" to "Drawdown is a measurement of decline from an asset/user’s highest point to its lowest point, in a given time.")

val inputData = mutableMapOf(
    "return" to arrayListOf(
        "Low (0%-10%)" to listOf("0", "10"),
        "Medium (10%-50%)" to listOf("10", "50"),
        "High (Over 50%)" to listOf("50", null)
    ),
    "risk" to arrayListOf(
        "Low (0-2)" to listOf("0", "2"),
        "Medium (3-6)" to listOf("3", "6"),
        "High (Over 6)" to listOf("6", null)
    ),
    "numberCopy" to arrayListOf(
        "Low (0-2)" to listOf("0", "2"),
        "Medium (3-6)" to listOf("3", "6"),
        "High (Over 6)" to listOf("6", null)
    ),
    "active" to arrayListOf(
        "Low (0-2)" to listOf("0", "2"),
        "Medium (3-6)" to listOf("3", "6"),
        "High (Over 6)" to listOf("6", null)
    ),
    "profitable" to arrayListOf(
        "Low (0-2)" to listOf("0", "2"),
        "Medium (3-6)" to listOf("3", "6"),
        "High (Over 6)" to listOf("6", null)
    ),
    "numberTrades" to arrayListOf(
        "Low (0-2)" to listOf("0", "2"),
        "Medium (3-6)" to listOf("3", "6"),
        "High (Over 6)" to listOf("6", null)
    ),
    "daily" to arrayListOf(
        "Low (0-2)" to listOf("0", "2"),
        "Medium (3-6)" to listOf("3", "6"),
        "High (Over 6)" to listOf("6", null)
    ),
    "weekly" to arrayListOf(
        "Low (0-2)" to listOf("0", "2"),
        "Medium (3-6)" to listOf("3", "6"),
        "High (Over 6)" to listOf("6", null)
    ),
)



