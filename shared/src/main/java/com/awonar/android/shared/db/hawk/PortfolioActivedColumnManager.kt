package com.awonar.android.shared.db.hawk

import com.awonar.android.shared.utils.HawkUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PortfolioActivedColumnManager @Inject constructor(
    private val hawk: HawkUtil
) {
    companion object {
        const val MANUAL_ACTIVED_COLUMNS =
            "com.awonar.android.shared.db.hawk.hawk.portfolio.manual_actived_columns"
        const val MARKET_ACTIVED_COLUMNS =
            "com.awonar.android.shared.db.hawk.hawk.portfolio.market_actived_columns"
    }

    fun saveMarketColumn(activedList: List<String>) {
        hawk.put(MARKET_ACTIVED_COLUMNS, activedList)
    }

    fun saveManualColumn(activedList: List<String>) {
        hawk.put(MANUAL_ACTIVED_COLUMNS, activedList)
    }

    fun getManual(): List<String>? = hawk.get<List<String>>(MANUAL_ACTIVED_COLUMNS)
    fun getMarket(): List<String>? = hawk.get<List<String>>(MARKET_ACTIVED_COLUMNS)

    fun clearManual() {
        hawk.delete(MANUAL_ACTIVED_COLUMNS)
    }

    fun clearMarket() {
        hawk.delete(MARKET_ACTIVED_COLUMNS)
    }

}
