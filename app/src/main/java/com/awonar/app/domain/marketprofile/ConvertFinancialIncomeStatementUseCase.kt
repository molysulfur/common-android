package com.awonar.app.domain.marketprofile

import com.awonar.android.shared.di.MainDispatcher
import com.awonar.app.models.marketprofile.ConvertFinancial
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import java.util.*
import javax.inject.Inject

class ConvertFinancialIncomeStatementUseCase @Inject constructor(
    @MainDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<ConvertFinancial, MutableList<FinancialMarketItem>>(dispatcher) {
    override suspend fun execute(parameters: ConvertFinancial): MutableList<FinancialMarketItem> {
        val itemLists = mutableListOf<FinancialMarketItem>()
        val financial = parameters.financial
        itemLists.add(FinancialMarketItem.TitleMarketItem("Income Statement"))
        itemLists.add(FinancialMarketItem.BarChartItem(parameters.defaultSet))
        val incomeInfo = if (parameters.quarterType == "annual") {
            val year = Calendar.getInstance().apply {
                add(Calendar.YEAR, -1)
            }.get(Calendar.YEAR)
            itemLists.add(FinancialMarketItem.DropdownItem("Select Year",
                parameters.fiscal,
                arrayListOf("$year", "${year.minus(1)}", "${year.minus(2)}", "${year.minus(3)}")))
            financial?.incomeStatement?.get("year")?.find {
                it["fiscalYear"] == parameters.fiscal
            }
        } else {
            itemLists.add(FinancialMarketItem.DropdownItem("Select Quarter",
                parameters.fiscal,
                arrayListOf("Q1", "Q2", "Q3", "Q4")))
            financial?.incomeStatement?.get("quarter")?.find {
                it["fiscalPeriod"] == parameters.quarter
            }
        }
        convertListItems(incomeInfo, itemLists, parameters)
        return itemLists
    }

    private fun convertListItems(
        incomeInfo: Map<String, String?>?,
        itemLists: MutableList<FinancialMarketItem>,
        parameters: ConvertFinancial,
    ) {
        incomeInfo?.let {
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Benefits Costs and Expenses",
                key = "benefitsCostsExpenses",
                value = it["benefitsCostsExpenses"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "benefitsCostsExpenses" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Cost Of Revenue",
                key = "costOfRevenue",
                value = it["costOfRevenue"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "costOfRevenue" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Costs And Expenses",
                key = "costsAndExpenses",
                value = it["costsAndExpenses"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "costsAndExpenses" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Gross Profit",
                key = "grossProfit",
                value = it["grossProfit"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "grossProfit" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Income Tax Expense/Benefit",
                key = "incomeTaxExpenseBenefit",
                value = it["incomeTaxExpenseBenefit"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "incomeTaxExpenseBenefit" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Income Tax Expense/Benefit, Deferred",
                key = "incomeTaxExpenseBenefitDeferred",
                value = it["incomeTaxExpenseBenefitDeferred"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "incomeTaxExpenseBenefitDeferred" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Income/Loss From Continuing Operations After Tax",
                key = "incomeLossFromContinuingOperationsAfterTax",
                value = it["incomeLossFromContinuingOperationsAfterTax"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "incomeLossFromContinuingOperationsAfterTax" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Income/Loss From Continuing Operations Before Tax",
                key = "incomeLossFromContinuingOperationsBeforeTax",
                value = it["incomeLossFromContinuingOperationsBeforeTax"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "incomeLossFromContinuingOperationsBeforeTax" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Interest Expense, Operating",
                key = "interestExpenseOperating",
                value = it["interestExpenseOperating"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "interestExpenseOperating" }

            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Income/Loss",
                key = "netIncomeLoss",
                value = it["netIncomeLoss"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "netIncomeLoss" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Income/Loss Attributable To Noncontrolling Interest",
                key = "netIncomeLossAttributableToNoncontrollingInterest",
                value = it["netIncomeLossAttributableToNoncontrollingInterest"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "netIncomeLossAttributableToNoncontrollingInterest" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Income/Loss Attributable To Parent",
                key = "netIncomeLossAttributableToParent",
                value = it["netIncomeLossAttributableToParent"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "netIncomeLossAttributableToParent" }

            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Income/Loss Available To Common Stockholders, Basic",
                key = "netIncomeLossAvailableToCommonStockholdersBasic",
                value = it["netIncomeLossAvailableToCommonStockholdersBasic"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "netIncomeLossAvailableToCommonStockholdersBasic" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Nonoperating Income/Loss",
                key = "nonoperatingIncomeLoss",
                value = it["nonoperatingIncomeLoss"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "nonoperatingIncomeLoss" }

            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Operating Expenses",
                key = "operatingExpenses",
                value = it["operatingExpenses"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "operatingExpenses" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Operating Income/Loss",
                key = "operatingIncomeLoss",
                value = it["operatingIncomeLoss"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "operatingIncomeLoss" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Participating Securities, Distributed And Undistributed Earnings/Loss, Basic",
                key = "participatingSecuritiesDistributedAndUndistributedEarningsLossBasic",
                value = it["participatingSecuritiesDistributedAndUndistributedEarningsLossBasic"]?.toLong()
                    ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "participatingSecuritiesDistributedAndUndistributedEarningsLossBasic" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Preferred Stock Dividends And Other Adjustments",
                key = "preferredStockDividendsAndOtherAdjustments",
                value = it["preferredStockDividendsAndOtherAdjustments"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "preferredStockDividendsAndOtherAdjustments" }
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Revenues",
                key = "revenues",
                value = it["revenues"]?.toLong() ?: 0L,
                color = parameters.defaultSet.indexOfFirst { item -> item.key == "revenues" }
            ))
        }
    }

}