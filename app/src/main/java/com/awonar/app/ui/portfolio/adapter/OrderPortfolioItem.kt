package com.awonar.app.ui.portfolio.adapter

import android.os.Parcelable
import com.awonar.android.model.portfolio.Position
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioType.INSTRUMENT_PORTFOLIO
import kotlinx.parcelize.Parcelize

@Parcelize
data class ColumnValue(
    var value: Float,
    var type: ColumnValueType,
) : Parcelable

enum class ColumnValueType {
    COLUMN_TEXT,
    COLUMN_BUTTON,
    COLUMN_CURRENT,
    COLUMN_PIP_CHANGE,
    COLUMN_VALUE,
    PROFITLOSS,
    PROFITLOSS_PERCENT
}

sealed class OrderPortfolioItem(
    val type: Int,
    open val value1: ColumnValue? = null,
    open val value2: ColumnValue? = null,
    open val value3: ColumnValue? = null,
    open val value4: ColumnValue? = null
) : Parcelable {

    @Parcelize
    class InstrumentPortfolioItem(
        val position: Position,
        val conversionRate: Float,
        override val value1: ColumnValue? = null,
        override val value2: ColumnValue? = null,
        override val value3: ColumnValue? = null,
        override val value4: ColumnValue? = null
    ) : OrderPortfolioItem(INSTRUMENT_PORTFOLIO)
}