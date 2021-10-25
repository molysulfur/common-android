package com.awonar.android.shared.db.hawk

import com.awonar.android.model.user.User
import com.awonar.android.shared.utils.HawkUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PortfolioActivedColumnManager @Inject constructor(
    private val hawk: HawkUtil
) {
    companion object {
        const val PORTFOLIO_ACTIVED_COLUMNS = "com.awonar.android.shared.db.hawk.hawk.portfolio_actived_columns"
    }

    fun save(activedList: List<String>) {
        hawk.put(PORTFOLIO_ACTIVED_COLUMNS, activedList)
    }

    fun get(): List<String>? = hawk.get<List<String>>(PORTFOLIO_ACTIVED_COLUMNS)

    fun clear() {
        hawk.delete(PORTFOLIO_ACTIVED_COLUMNS)
    }

}
