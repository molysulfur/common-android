package com.awonar.app.domain.marketprofile

import com.awonar.android.shared.di.MainDispatcher
import com.awonar.app.models.marketprofile.ConvertFinancial
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertFinancialCashflowUseCase @Inject constructor(
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
        val incomeInfo = financial?.cashFlow?.quarter?.get(0)
        incomeInfo?.let {
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow",
                value = it.netCashFlow,
                color = 0,
                isSelected = false
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow From Financing Activities",
                value = it.netCashFlowFromFinancingActivities,
                color = 0,
                isSelected = false
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow From Financing Activities, Continuing",
                value = it.netCashFlowFromFinancingActivitiesContinuing,
                color = 0,
                isSelected = false
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow From Investing Activities",
                value = it.netCashFlowFromInvestingActivities,
                color = 0,
                isSelected = false
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow From Investing Activities, Continuing",
                value = it.netCashFlowFromInvestingActivitiesContinuing,
                color = 0,
                isSelected = false
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow From Operating Activities",
                value = it.netCashFlowFromOperatingActivities,
                color = 0,
                isSelected = false
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow From Operating Activities, Continuing",
                value = it.netCashFlowFromOperatingActivitiesContinuing,
                color = 0,
                isSelected = false
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow, Continuing",
                value = it.netCashFlowContinuing,
                color = 0,
                isSelected = false
            ))

        }
        return itemLists
    }

}