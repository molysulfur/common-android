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
        itemLists.add(FinancialMarketItem.TitleMarketItem("Income Statement"))
        itemLists.add(FinancialMarketItem.BarChartItem(
            arrayListOf(
                FinancialMarketItem.BarEntryItem(
                    "grossMargin",
                    "Gross Margin",
                    incomeGrossBarEntries
                ),
                FinancialMarketItem.BarEntryItem(
                    "operatingMargin",
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
        itemLists.add(FinancialMarketItem.TitleMarketItem("Balanace Sheet"))
        itemLists.add(FinancialMarketItem.BarChartItem(
            arrayListOf(FinancialMarketItem.BarEntryItem(
                "currentRatio",
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
        itemLists.add(FinancialMarketItem.TitleMarketItem("Cashflow"))
        itemLists.add(FinancialMarketItem.BarChartItem(
            arrayListOf(
                FinancialMarketItem.BarEntryItem(
                    "operatingCashFlow",
                    "Operating Cashflow",
                    operateCashflow
                )
            )))

        return itemLists
    }

}