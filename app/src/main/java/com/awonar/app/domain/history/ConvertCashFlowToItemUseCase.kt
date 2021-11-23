package com.awonar.app.domain.history

import com.awonar.android.model.history.HistoryCashFlow
import com.awonar.android.shared.di.MainDispatcher
import com.awonar.app.R
import com.awonar.app.ui.history.adapter.HistoryItem
import com.awonar.app.utils.DateUtils
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertCashFlowToItemUseCase @Inject constructor(
    @MainDispatcher dispatcher: CoroutineDispatcher
) : UseCase<List<HistoryCashFlow>, MutableList<HistoryItem>>(dispatcher) {
    override suspend fun execute(parameters: List<HistoryCashFlow>): MutableList<HistoryItem> {
        val itemList = mutableListOf<HistoryItem>()
        parameters.forEach {
            itemList.add(
                HistoryItem.CashFlowItem(
                    logo = createLogo(it.transactionType),
                    title = createTitle(it.transactionType),
                    subTitle = "${DateUtils.getDate(it.transactionDate,"dd/MM/YYYY")} • #${it.transactionNo}",
                    id = it.cashflow?.id,
                    amount = it.cashflow?.dollarAmount ?: 0f,
                    status = it.cashflow?.status,
                    description = createDescription(it.transactionType, it.transactionDate),
                    fee = it.cashflow?.fee ?: 0f,
                    netWithdraw = it.cashflow?.dollarAmount?.minus(it.cashflow?.fee ?: 0f) ?: 0f,
                    rate = it.cashflow?.localRate ?: 0f,
                    localAmount = it.cashflow?.localAmount ?: 0f
                )
            )
            itemList.add(HistoryItem.DividerItem())
        }
        return itemList
    }

    private fun createDescription(transactionType: Int, transactionDate: String?): String =
        when (transactionType) {
            3 -> "Your withdrawal request has been processed. Please note that payments made by bank transfer and/or credit card may take up to 8 business days for the funds to appear in your account."
            4 -> "Your request has been submitted. Once approved, it should be processed within one to two business days"
            5 -> "Your withdrawal was cancelled on %s This cancellation is irreversible. If you still want to withdraw funds, please open a new withdrawal request.".format(
                DateUtils.getDate(transactionDate)
            )
            else -> ""
        }

    private fun createTitle(transactionType: Int): String = when (transactionType) {
        2 -> "Deposit"
        3 -> "Withdraw"
        5 -> "Reverse Withdraw"
        7 -> "Compoensation"
        8 -> "Reduct"
        else -> ""
    }

    private fun createLogo(transactionType: Int) = when (transactionType) {
        2 -> R.drawable.awonar_ic_deposit
        3 -> R.drawable.awonar_ic_deposit_success
        4 -> R.drawable.awonar_ic_deposit_success
        5 -> R.drawable.awoanr_ic_deposit_reverse
        6 -> R.drawable.awoanr_ic_deposit_cenceled
        7 -> R.drawable.awonar_ic_deposit_compensation
        else -> 0
    }

}