package com.awonar.app.ui.portfolio.adapter

import android.os.Parcelable
import com.awonar.android.model.portfolio.Position
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioType.INSTRUMENT_PORTFOLIO
import kotlinx.parcelize.Parcelize

sealed class OrderPortfolioItem(val type: Int) : Parcelable {


    @Parcelize
    class InstrumentPortfolioItem(val position: Position) : OrderPortfolioItem(INSTRUMENT_PORTFOLIO)
}