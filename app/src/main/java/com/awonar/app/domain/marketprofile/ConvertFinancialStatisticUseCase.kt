package com.awonar.app.domain.marketprofile

import com.awonar.android.shared.di.MainDispatcher
import com.awonar.app.models.marketprofile.ConvertFinancial
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.github.mikephil.charting.data.BarEntry
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertFinancialStatisticUseCase @Inject constructor(
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
        val menuTabs = arrayListOf("Statistic",
            "Income Statement",
            "Balance Sheet",
            "Cashflow")
        itemLists.add(FinancialMarketItem.TabsItem(0, menuTabs))
        /**
         * income statistic
         */
        val incomeGrossBarEntries = mutableListOf<BarEntry>()
        val incomeOperateBarEntries = mutableListOf<BarEntry>()
        financial?.ststistics?.quarter?.forEachIndexed { index, financialQuarter ->
            val grossY = if (financialQuarter.grossMargin == "N/A") {
                0f
            } else {
                financialQuarter.grossMargin?.toFloat() ?: 0f
            }
            val operateY = financialQuarter.operatingMargin
            incomeOperateBarEntries.add(BarEntry(index.toFloat(), operateY))
            incomeGrossBarEntries.add(BarEntry(index.toFloat(), grossY))
        }
        itemLists.add(FinancialMarketItem.BarChartItem(
            arrayListOf(
                FinancialMarketItem.BarEntryItem(
                    "Gross Margin",
                    incomeGrossBarEntries
                ),
                FinancialMarketItem.BarEntryItem(
                    "Operating Margin",
                    incomeOperateBarEntries
                ))
        ))
        /**
         * Balanace Sheet
         */
        val currentRatioEntries = mutableListOf<BarEntry>()
        financial?.ststistics?.quarter?.forEachIndexed { index, financialQuarter ->
            currentRatioEntries.add(BarEntry(index.toFloat(),
                financialQuarter.currentRatio))
        }
        itemLists.add(FinancialMarketItem.BarChartItem(
            arrayListOf(FinancialMarketItem.BarEntryItem(
                "Current Ratio",
                currentRatioEntries
            ))))
        /**
         * Cashflow
         */
        val operateCashflow = mutableListOf<BarEntry>()
        financial?.ststistics?.quarter?.forEachIndexed { index, financialQuarter ->
            operateCashflow.add(BarEntry(index.toFloat(),
                financialQuarter.operatingCashFlow))
        }
        itemLists.add(FinancialMarketItem.BarChartItem(
            arrayListOf(
                FinancialMarketItem.BarEntryItem(
                    "Operating Cashflow",
                    operateCashflow
                )
            )))

        return itemLists
    }

}