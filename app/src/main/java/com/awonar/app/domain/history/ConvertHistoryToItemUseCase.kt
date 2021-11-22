package com.awonar.app.domain.history

import com.awonar.android.model.history.History
import com.awonar.android.shared.di.MainDispatcher
import com.awonar.app.ui.history.adapter.HistoryItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertHistoryToItemUseCase @Inject constructor(
    @MainDispatcher dispatcher: CoroutineDispatcher
) : UseCase<MutableList<History>, MutableList<HistoryItem>>(dispatcher) {
    override suspend fun execute(parameters: MutableList<History>): MutableList<HistoryItem> {
        return parameters.map {
            when (it.transactionType) {
                1 -> HistoryItem.PositionItem(
                    invested = it.amount,
                    picture = it.position?.instrument?.logo,
                    pl = it.position?.netProfit ?: 0f,
                    plPercent = it.position?.netProfit?.times(100)?.div(it.amount) ?: 0f,
                    detail = "${if (it.position?.isBuy == true) "BUY" else "SELL"} ${it.position?.instrument?.symbol}",
                    transactionType = it.transactionType,
                    history = it,
                    master = it.master,
                    positionType = "manual",
                )
                11 -> HistoryItem.PositionItem(
                    invested = it.amount,
                    picture = it.master?.picture,
                    pl = it.position?.netProfit ?: 0f,
                    plPercent = it.position?.netProfit?.times(100)?.div(it.amount) ?: 0f,
                    detail = it.detail,
                    transactionType = it.transactionType,
                    history = it,
                    master = it.master,
                    positionType = "user"
                )
                else -> HistoryItem.PositionItem(
                    invested = it.amount,
                    picture = it.master?.picture,
                    pl = it.position?.netProfit ?: 0f,
                    plPercent = it.position?.netProfit?.times(100)?.div(it.amount) ?: 0f,
                    detail = it.detail,
                    transactionType = it.transactionType,
                    history = it,
                    master = it.master
                )
            }
        }.toMutableList()
    }
}