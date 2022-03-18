package com.awonar.app.domain.marketprofile

import com.awonar.android.shared.di.MainDispatcher
import com.awonar.app.models.marketprofile.ConvertFinancial
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.github.mikephil.charting.data.BarEntry
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
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
        itemLists.add(FinancialMarketItem.ButtonGroupItem("annual", "quarter", ""))
        itemLists.add(FinancialMarketItem.TabsItem(arrayListOf("Statistic",
            "Income Statement",
            "Balance Sheet",
            "Cashflow"), parameters.type))
        itemLists.add(FinancialMarketItem.BarChartItem(parameters.defaultSet))
        val incomeInfo = financial?.incomeStatement?.quarter?.get(0)
        incomeInfo?.let {
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Benefits Costs and Expenses",
                value = it.benefitsCostsExpenses,
                color = 0,
                isSelected = parameters.defaultSet.find { item -> item.title == "Benefits Costs and Expenses" } != null
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Cost Of Revenue",
                value = it.costOfRevenue,
                color = 0,
                isSelected = parameters.defaultSet.find { item -> item.title == "Cost Of Revenue" } != null
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Costs And Expenses",
                value = it.costsAndExpenses,
                color = 0,
                isSelected = parameters.defaultSet.find { item -> item.title == "Costs And Expenses" } != null
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Gross Profit",
                value = it.grossProfit,
                color = 0,
                isSelected = parameters.defaultSet.find { item -> item.title == "Gross Profit" } != null
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Income Tax Expense/Benefit",
                value = it.incomeTaxExpenseBenefit,
                color = 0,
                isSelected = parameters.defaultSet.find { item -> item.title == "Income Tax Expense/Benefit" } != null
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Income Tax Expense/Benefit, Deferred",
                value = it.incomeTaxExpenseBenefitDeferred,
                color = 0,
                isSelected = parameters.defaultSet.find { item -> item.title == "Income Tax Expense/Benefit, Deferred" } != null
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Income/Loss From Continuing Operations After Tax",
                value = it.incomeLossFromContinuingOperationsAfterTax,
                color = 0,
                isSelected = parameters.defaultSet.find { item -> item.title == "Income/Loss From Continuing Operations After Tax" } != null
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Income/Loss From Continuing Operations Before Tax",
                value = it.incomeLossFromContinuingOperationsBeforeTax,
                color = 0,
                isSelected = parameters.defaultSet.find { item -> item.title == "Income/Loss From Continuing Operations Before Tax" } != null
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Interest Expense, Operating",
                value = it.interestExpenseOperating,
                color = 0,
                isSelected = parameters.defaultSet.find { item -> item.title == "Interest Expense, Operating" } != null

            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Income/Loss",
                value = it.netIncomeLoss,
                color = 0,
                isSelected = parameters.defaultSet.find { item -> item.title == "Net Income/Loss" } != null
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Income/Loss Attributable To Noncontrolling Interest",
                value = it.netIncomeLossAttributableToNoncontrollingInterest,
                color = 0,
                isSelected = parameters.defaultSet.find { item -> item.title == "Net Income/Loss Attributable To Noncontrolling Interest" } != null
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Income/Loss Attributable To Parent",
                value = it.netIncomeLossAttributableToParent,
                color = 0,
                isSelected = parameters.defaultSet.find { item -> item.title == "Net Income/Loss Attributable To Parent" } != null

            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Net Income/Loss Available To Common Stockholders, Basic",
                value = it.netIncomeLossAvailableToCommonStockholdersBasic,
                color = 0,
                isSelected = parameters.defaultSet.find { item -> item.title == "Net Income/Loss Available To Common Stockholders, Basic" } != null
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Nonoperating Income/Loss",
                value = it.nonoperatingIncomeLoss,
                color = 0,
                isSelected = parameters.defaultSet.find { item -> item.title == "Nonoperating Income/Loss" } != null

            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Operating Expenses",
                value = it.operatingExpenses,
                color = 0,
                isSelected = parameters.defaultSet.find { item -> item.title == "Operating Expenses" } != null
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Operating Income/Loss",
                value = it.operatingIncomeLoss,
                color = 0,
                isSelected = parameters.defaultSet.find { item -> item.title == "Operating Income/Loss" } != null
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Participating Securities, Distributed And Undistributed Earnings/Loss, Basic",
                value = it.participatingSecuritiesDistributedAndUndistributedEarningsLossBasic,
                color = 0,
                isSelected = parameters.defaultSet.find { item -> item.title == "Participating Securities, Distributed And Undistributed Earnings/Loss, Basic" } != null
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Preferred Stock Dividends And Other Adjustments",
                value = it.preferredStockDividendsAndOtherAdjustments,
                color = 0,
                isSelected = parameters.defaultSet.find { item -> item.title == "Preferred Stock Dividends And Other Adjustments" } != null
            ))
            itemLists.add(FinancialMarketItem.ListSelectorItem(
                text = "Revenues",
                value = it.revenues,
                color = 0,
                isSelected = parameters.defaultSet.find { item -> item.title == "Revenues" } != null
            ))
        }
        return itemLists
    }

}