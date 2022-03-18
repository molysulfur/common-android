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
        itemLists.add(FinancialMarketItem.TabsItem(arrayListOf("Statistic",
            "Income Statement",
            "Balance Sheet",
            "Cashflow"), parameters.type))
        val incomeInfo = financial?.balanceSheet?.quarter?.get(0)
        incomeInfo?.let {
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Assets",
                value = it.assets,
                color = 0,
                isSelected = false
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Current Assets",
                value = it.currentAssets,
                color = 0,
                isSelected = false
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Current Liabilities",
                value = it.currentLiabilities,
                color = 0,
                isSelected = false
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Equity",
                value = it.equity,
                color = 0,
                isSelected = false
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Equity Attributable To Noncontrolling Interest",
                value = it.equityAttributableToNoncontrollingInterest,
                color = 0,
                isSelected = false
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Equity Attributable To Parent",
                value = it.equityAttributableToParent,
                color = 0,
                isSelected = false
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Fixed Assets",
                value = it.fixedAssets,
                color = 0,
                isSelected = false
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Liabilities",
                value = it.liabilities,
                color = 0,
                isSelected = false
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Liabilities And Equity",
                value = it.liabilitiesAndEquity,
                color = 0,
                isSelected = false
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Noncurrent Assets",
                value = it.noncurrentAssets,
                color = 0,
                isSelected = false
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Noncurrent Liabilities",
                value = it.noncurrentLiabilities,
                color = 0,
                isSelected = false
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Other Than Fixed Noncurrent Assets",
                value = it.otherThanFixedNoncurrentAssets,
                color = 0,
                isSelected = false
            ))
        }
        return itemLists
    }

}