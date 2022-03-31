package com.awonar.app.domain.marketprofile

import com.awonar.android.shared.di.MainDispatcher
import com.awonar.app.models.marketprofile.ConvertFinancial
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import java.util.*
import javax.inject.Inject

class ConvertFinancialCashflowUseCase @Inject constructor(
    @MainDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<ConvertFinancial, MutableList<FinancialMarketItem>>(dispatcher) {
    override suspend fun execute(parameters: ConvertFinancial): MutableList<FinancialMarketItem> {
        val itemLists = mutableListOf<FinancialMarketItem>()
        val financial = parameters.financial
        itemLists.add(FinancialMarketItem.TitleMarketItem("Cashflow"))
        itemLists.add(FinancialMarketItem.BarChartItem(parameters.defaultSet))
        val cashflow = if (parameters.quarterType == "annual") {
            val year = Calendar.getInstance().apply {
                add(Calendar.YEAR, -1)
            }.get(Calendar.YEAR)
            itemLists.add(FinancialMarketItem.DropdownItem("Select Year",
                parameters.fiscal,
                arrayListOf("$year", "${year.minus(1)}", "${year.minus(2)}", "${year.minus(3)}")))

            financial?.cashFlow?.get("year")?.find { it["fiscalYear"] == parameters.fiscal }
        } else {
            itemLists.add(FinancialMarketItem.DropdownItem("Select Quarter",
                parameters.fiscal,
                arrayListOf("Q1", "Q2", "Q3", "Q4")))
            financial?.cashFlow?.get("quarter")?.find {
                it["fiscalPeriod"] == parameters.quarter
            }
        }
        convertListItems(cashflow, itemLists, parameters)
        return itemLists
    }

    private fun convertListItems(
        incomeInfo: Map<String, String?>?,
        itemLists: MutableList<FinancialMarketItem>,
        parameters: ConvertFinancial,
    ) {
        incomeInfo?.let {
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow",
                key = "netCashFlow",
                value = it["netCashFlow"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "netCashFlow" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow From Financing Activities",
                key = "netCashFlowFromFinancingActivities",
                value = it["netCashFlowFromFinancingActivities"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "netCashFlowFromFinancingActivities" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow From Financing Activities, Continuing",
                key = "netCashFlowFromFinancingActivitiesContinuing",
                value = it["netCashFlowFromFinancingActivitiesContinuing"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "netCashFlowFromFinancingActivitiesContinuing" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow From Investing Activities",
                key = "netCashFlowFromInvestingActivities",
                value = it["netCashFlowFromInvestingActivities"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "netCashFlowFromInvestingActivities" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow From Investing Activities, Continuing",
                key = "netCashFlowFromInvestingActivitiesContinuing",
                value = it["netCashFlowFromInvestingActivitiesContinuing"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "netCashFlowFromInvestingActivitiesContinuing" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow From Operating Activities",
                key = "netCashFlowFromOperatingActivities",
                value = it["netCashFlowFromOperatingActivities"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "netCashFlowFromOperatingActivities" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow From Operating Activities, Continuing",
                key = "netCashFlowFromOperatingActivitiesContinuing",
                value = it["netCashFlowFromOperatingActivitiesContinuing"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "netCashFlowFromOperatingActivitiesContinuing" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow, Continuing",
                key = "netCashFlowContinuing",
                value = it["netCashFlowContinuing"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "netCashFlowContinuing" }
            ))

        }
    }

}