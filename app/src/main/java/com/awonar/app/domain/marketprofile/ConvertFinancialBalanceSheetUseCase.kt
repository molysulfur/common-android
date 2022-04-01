package com.awonar.app.domain.marketprofile

import com.awonar.android.shared.di.MainDispatcher
import com.awonar.app.models.marketprofile.ConvertFinancial
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import java.util.*
import javax.inject.Inject

class ConvertFinancialBalanceSheetUseCase @Inject constructor(
    @MainDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<ConvertFinancial, MutableList<FinancialMarketItem>>(dispatcher) {
    override suspend fun execute(parameters: ConvertFinancial): MutableList<FinancialMarketItem> {
        val itemLists = mutableListOf<FinancialMarketItem>()
        val financial = parameters.financial

        itemLists.add(FinancialMarketItem.TitleMarketItem("Balance Sheet"))
        itemLists.add(FinancialMarketItem.BarChartItem(parameters.defaultSet))
        val balanceInfo = if (parameters.quarterType == "annual") {
            financial?.balanceSheet?.get("year")?.find { it["fiscalYear"] == parameters.fiscal }
        } else {
            financial?.balanceSheet?.get("quarter")?.find {
                it["fiscalPeriod"] == parameters.quarter
            }
        }

        convertListItems(balanceInfo, itemLists, parameters)
        return itemLists
    }

    private fun convertListItems(
        incomeInfo: Map<String, String?>?,
        itemLists: MutableList<FinancialMarketItem>,
        parameters: ConvertFinancial,
    ) {
        incomeInfo?.let {
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                key = "assets",
                text = "Assets",
                value = it["assets"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "assets" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Current Assets",
                key = "currentAssets",
                value = it["currentAssets"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "currentAssets" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Current Liabilities",
                key = "currentLiabilities",
                value = it["currentLiabilities"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "currentLiabilities" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Equity",
                key = "equity",
                value = it["equity"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "equity" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Equity Attributable To Noncontrolling Interest",
                key = "equityAttributableToNoncontrollingInterest",
                value = it["equityAttributableToNoncontrollingInterest"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "equityAttributableToNoncontrollingInterest" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Equity Attributable To Parent",
                key = "equityAttributableToParent",
                value = it["equityAttributableToParent"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "equityAttributableToParent" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Fixed Assets",
                key = "fixedAssets",
                value = it["fixedAssets"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "fixedAssets" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Liabilities",
                key = "liabilities",
                value = it["liabilities"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "liabilities" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Liabilities And Equity",
                key = "liabilitiesAndEquity",
                value = it["liabilitiesAndEquity"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "liabilitiesAndEquity" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Noncurrent Assets",
                key = "noncurrentAssets",
                value = it["noncurrentAssets"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "noncurrentAssets" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Noncurrent Liabilities",
                key = "noncurrentLiabilities",
                value = it["noncurrentLiabilities"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "noncurrentLiabilities" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Other Than Fixed Noncurrent Assets",
                key = "otherThanFixedNoncurrentAssets",
                value = it["otherThanFixedNoncurrentAssets"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "otherThanFixedNoncurrentAssets" }
            ))
        }
    }

}