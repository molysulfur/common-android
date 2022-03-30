package com.awonar.app.domain.marketprofile

import com.awonar.android.model.marketprofile.FinancialResponse
import com.awonar.android.shared.di.MainDispatcher
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertFinancialToCardItemUseCase @Inject constructor(
    @MainDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<FinancialResponse, MutableList<FinancialMarketItem>>(dispatcher) {
    override suspend fun execute(parameters: FinancialResponse): MutableList<FinancialMarketItem> {
        val itemLists = mutableListOf<FinancialMarketItem>()
        itemLists.add(FinancialMarketItem.FinancialCardItem(
            "Split Event",
            parameters.ststistics.split.historical[0].date,
            "%s:%s".format(parameters.ststistics.split.historical[0].denominator,
                parameters.ststistics.split.historical[0].numerator)
        ))
        itemLists.add(FinancialMarketItem.FinancialCardItem(
            "Divided",
            parameters.ststistics.dividend.historical[0].date,
            "%.2f".format(parameters.ststistics.dividend.historical[0].dividend)
        ))
        return itemLists
    }
}