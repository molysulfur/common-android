package com.awonar.app.domain.marketprofile

import com.awonar.android.model.marketprofile.Income
import com.awonar.android.shared.di.MainDispatcher
import com.awonar.app.models.marketprofile.ConvertFinancial
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ConvertFinancialIncomeStatementUseCase @Inject constructor(
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
        itemLists.add(FinancialMarketItem.TitleMarketItem("Income Statement"))
        itemLists.add(FinancialMarketItem.BarChartItem(parameters.defaultSet))
        val incomeInfo = if (parameters.quarterType == "annual") {
            val year = Calendar.getInstance().apply {
                add(Calendar.YEAR, -1)
            }.get(Calendar.YEAR)
            itemLists.add(FinancialMarketItem.DropdownItem("Select Year",
                parameters.fiscal,
                arrayListOf("$year", "${year.minus(1)}", "${year.minus(2)}", "${year.minus(3)}")))
            financial?.incomeStatement?.year?.find {
                it.fiscalYear == parameters.fiscal
            }
        } else {
            itemLists.add(FinancialMarketItem.DropdownItem("Select Quarter",
                parameters.fiscal,
                arrayListOf("Q1", "Q2", "Q3", "Q4")))
            financial?.incomeStatement?.quarter?.find {
                it.fiscalPeriod == parameters.quarter
            }
        }
        convertListItems(incomeInfo, itemLists, parameters)
        return itemLists
    }

    private fun convertListItems(
        incomeInfo: Income?,
        itemLists: MutableList<FinancialMarketItem>,
        parameters: ConvertFinancial,
    ) {
        incomeInfo?.let {
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Benefits Costs and Expenses",
                value = it.benefitsCostsExpenses,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Benefits Costs and Expenses" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Cost Of Revenue",
                value = it.costOfRevenue,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Cost Of Revenue" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Costs And Expenses",
                value = it.costsAndExpenses,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Costs And Expenses" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Gross Profit",
                value = it.grossProfit,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Gross Profit" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Income Tax Expense/Benefit",
                value = it.incomeTaxExpenseBenefit,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Income Tax Expense/Benefit" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Income Tax Expense/Benefit, Deferred",
                value = it.incomeTaxExpenseBenefitDeferred,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Income Tax Expense/Benefit, Deferred" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Income/Loss From Continuing Operations After Tax",
                value = it.incomeLossFromContinuingOperationsAfterTax,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Income/Loss From Continuing Operations After Tax" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Income/Loss From Continuing Operations Before Tax",
                value = it.incomeLossFromContinuingOperationsBeforeTax,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Income/Loss From Continuing Operations Before Tax" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Interest Expense, Operating",
                value = it.interestExpenseOperating,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Interest Expense, Operating" }

            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Income/Loss",
                value = it.netIncomeLoss,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Net Income/Loss" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Income/Loss Attributable To Noncontrolling Interest",
                value = it.netIncomeLossAttributableToNoncontrollingInterest,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Net Income/Loss Attributable To Noncontrolling Interest" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Income/Loss Attributable To Parent",
                value = it.netIncomeLossAttributableToParent,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Net Income/Loss Attributable To Parent" }

            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Income/Loss Available To Common Stockholders, Basic",
                value = it.netIncomeLossAvailableToCommonStockholdersBasic,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Net Income/Loss Available To Common Stockholders, Basic" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Nonoperating Income/Loss",
                value = it.nonoperatingIncomeLoss,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Nonoperating Income/Loss" }

            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Operating Expenses",
                value = it.operatingExpenses,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Operating Expenses" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Operating Income/Loss",
                value = it.operatingIncomeLoss,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Operating Income/Loss" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Participating Securities, Distributed And Undistributed Earnings/Loss, Basic",
                value = it.participatingSecuritiesDistributedAndUndistributedEarningsLossBasic,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Participating Securities, Distributed And Undistributed Earnings/Loss, Basic" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Preferred Stock Dividends And Other Adjustments",
                value = it.preferredStockDividendsAndOtherAdjustments,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Preferred Stock Dividends And Other Adjustments" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Revenues",
                value = it.revenues,
                color = parameters.defaultSet.indexOfFirst { item -> item.title == "Revenues" }
            ))
        }
    }

}