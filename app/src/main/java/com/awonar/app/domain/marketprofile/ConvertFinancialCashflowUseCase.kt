package com.awonar.app.domain.marketprofile

import com.awonar.android.model.marketprofile.CashFlow
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
        itemLists.add(FinancialMarketItem.ButtonGroupItem("annual",
            "quarter",
            parameters.quarterType))
        itemLists.add(FinancialMarketItem.TabsItem(parameters.current))
        itemLists.add(FinancialMarketItem.TitleMarketItem("Cashflow"))
        itemLists.add(FinancialMarketItem.BarChartItem(parameters.defaultSet))
        val cashflow = if (parameters.quarterType == "annual") {
            val year = Calendar.getInstance().apply {
                add(Calendar.YEAR, -1)
            }.get(Calendar.YEAR)
            itemLists.add(FinancialMarketItem.DropdownItem("Select Year",
                parameters.fiscal,
                arrayListOf("$year", "${year.minus(1)}", "${year.minus(2)}", "${year.minus(3)}")))

            financial?.cashFlow?.year?.find { it.fiscalYear == parameters.fiscal }
        } else {
            itemLists.add(FinancialMarketItem.DropdownItem("Select Quarter",
                parameters.fiscal,
                arrayListOf("Q1", "Q2", "Q3", "Q4")))
            financial?.cashFlow?.quarter?.find {
                it.fiscalPeriod == parameters.quarter
            }
        }
        convertListItems(cashflow, itemLists, parameters)
        return itemLists
    }

    private fun convertListItems(
        incomeInfo: CashFlow?,
        itemLists: MutableList<FinancialMarketItem>,
        parameters: ConvertFinancial,
    ) {
        incomeInfo?.let {
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow",
                value = it.netCashFlow,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Net Cash Flow" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow From Financing Activities",
                value = it.netCashFlowFromFinancingActivities,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Net Cash Flow From Financing Activities" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow From Financing Activities, Continuing",
                value = it.netCashFlowFromFinancingActivitiesContinuing,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Net Cash Flow From Financing Activities, Continuing" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow From Investing Activities",
                value = it.netCashFlowFromInvestingActivities,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Net Cash Flow From Investing Activities" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow From Investing Activities, Continuing",
                value = it.netCashFlowFromInvestingActivitiesContinuing,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Net Cash Flow From Investing Activities, Continuing" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow From Operating Activities",
                value = it.netCashFlowFromOperatingActivities,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Net Cash Flow From Operating Activities" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow From Operating Activities, Continuing",
                value = it.netCashFlowFromOperatingActivitiesContinuing,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Net Cash Flow From Operating Activities, Continuing" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Cash Flow, Continuing",
                value = it.netCashFlowContinuing,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Net Cash Flow, Continuing" }
            ))

        }
    }

}