package com.awonar.app.domain.marketprofile

import com.awonar.android.shared.di.MainDispatcher
import com.awonar.app.models.marketprofile.ConvertFinancial
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertFinancialBalanceSheetUseCase @Inject constructor(
    @MainDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<ConvertFinancial, MutableList<FinancialMarketItem>>(dispatcher) {
    override suspend fun execute(parameters: ConvertFinancial): MutableList<FinancialMarketItem> {
        val itemLists = mutableListOf<FinancialMarketItem>()
        val financial = parameters.financial
        itemLists.add(FinancialMarketItem.FinancialCardItem(
            "Split Event",
            financial?.ststistics?.split?.historical?.get(0)?.date,
            "%s:%s".format(financial?.ststistics?.split?.historical?.get(0)?.denominator,
                financial?.ststistics?.split?.historical?.get(0)?.numerator)
        ))
        itemLists.add(FinancialMarketItem.FinancialCardItem(
            "Divided",
            financial?.ststistics?.dividend?.historical?.get(0)?.date,
            "%.2f".format(financial?.ststistics?.dividend?.historical?.get(0)?.dividend)
        ))
        itemLists.add(FinancialMarketItem.TitleMarketItem("Financial Summary"))
        itemLists.add(FinancialMarketItem.ButtonGroupItem("annual", "quarter", ""))
        itemLists.add(FinancialMarketItem.TabsItem(2, arrayListOf("Statistic",
            "Income Statement",
            "Balance Sheet",
            "Cashflow")))
        itemLists.add(FinancialMarketItem.BarChartItem(parameters.defaultSet))
        val incomeInfo = financial?.balanceSheet?.quarter?.get(0)
        incomeInfo?.let {
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Assets",
                value = it.assets,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Assets" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Current Assets",
                value = it.currentAssets,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Current Assets" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Current Liabilities",
                value = it.currentLiabilities,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Current Liabilities" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Equity",
                value = it.equity,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Equity" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Equity Attributable To Noncontrolling Interest",
                value = it.equityAttributableToNoncontrollingInterest,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Equity Attributable To Noncontrolling Interest" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Equity Attributable To Parent",
                value = it.equityAttributableToParent,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Equity Attributable To Parent" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Fixed Assets",
                value = it.fixedAssets,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Fixed Assets" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Liabilities",
                value = it.liabilities,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Liabilities" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Liabilities And Equity",
                value = it.liabilitiesAndEquity,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Liabilities And Equity" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Noncurrent Assets",
                value = it.noncurrentAssets,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Noncurrent Assets" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Noncurrent Liabilities",
                value = it.noncurrentLiabilities,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Noncurrent Liabilities" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Other Than Fixed Noncurrent Assets",
                value = it.otherThanFixedNoncurrentAssets,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Other Than Fixed Noncurrent Assets" }
            ))
        }
        return itemLists
    }

}