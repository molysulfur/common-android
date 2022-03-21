package com.awonar.app.models.marketprofile

import android.os.Parcelable
import com.awonar.android.model.marketprofile.FinancialResponse
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import kotlinx.parcelize.Parcelize


@Parcelize
data class ConvertFinancial(
    val financial: FinancialResponse?,
    val defaultSet: List<FinancialMarketItem.BarEntryItem> = emptyList(),
) : Parcelable