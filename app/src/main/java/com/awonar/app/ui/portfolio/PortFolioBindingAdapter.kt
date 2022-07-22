package com.awonar.app.ui.portfolio

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.portfolio.Copier
import com.awonar.app.R
import com.awonar.app.ui.portfolio.adapter.PortfolioAdapter
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.awonar.app.ui.portfolio.position.PositionViewModel
import com.awonar.app.utils.ColorChangingUtil
import com.awonar.app.widget.CopierPositionCardView
import com.awonar.app.widget.PositionView
import com.awonar.app.widget.InstrumentPositionCardView

@BindingAdapter("setUserPortfolio", "viewModel", "column1", "column2", "column3", "column4")
fun setUserPortfolio(
    recycler: RecyclerView,
    itemList: MutableList<PortfolioItem>,
    viewModel: PortFolioViewModel?,
    column1: String?,
    column2: String?,
    column3: String?,
    column4: String?,
) {
    if (recycler.adapter == null) {
        with(recycler) {
            adapter = PortfolioAdapter().apply {
                onIntrumentClick = { index ->
                    viewModel?.navigate(index)
                }
            }
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
        }
    }
    with(recycler.adapter as PortfolioAdapter) {
        itemLists = itemList
        columns = arrayListOf(column1 ?: "", column2 ?: "", column3 ?: "", column4 ?: "")
    }
}

@BindingAdapter("setAvaliable")
fun setAvaliable(
    textView: TextView,
    avaliable: Float,
) {
    with(textView) {
        text = "Avaliable: $%.2f".format(avaliable)
    }
}

@BindingAdapter("setAllocate")
fun setAllocate(
    textView: TextView,
    allocate: Float,
) {
    with(textView) {
        text = "Allocate: $%.2f".format(allocate)
    }
}


@BindingAdapter("setProfit")
fun setProfit(
    textView: TextView,
    profit: Float,
) {
    with(textView) {
        setTextColor(
            ColorChangingUtil.getTextColorChange(
                textView.context,
                profit
            )
        )
        text = "Profit: $%.2f".format(profit)
    }
}

@BindingAdapter("setEquity")
fun setEquity(
    textView: TextView,
    equity: Float,
) {
    with(textView) {
        text = "Equity: $%.2f".format(equity)
    }
}


@BindingAdapter("initCopierCard")
fun initCopierCard(
    view: CopierPositionCardView,
    item: PortfolioItem.CopierPositionCardItem?,
) {
    item?.let {
        view.setImage(item.copier.user.picture ?: "")
        view.setTitle("${item.copier.user.firstName} ${item.copier.user.middleName} ${item.copier.user.lastName}")
        view.setDescrption(item.copier.user.username ?: "")
        view.setInvested(item.invested)
        view.setValueInvested(item.value)
        view.setMoney(item.money)
        view.setAvgOpen(item.profitLoss)
        view.setProfitLoss(item.profitLoss)
    }
}

@BindingAdapter("initInstrumentPositionCard")
fun setInstrumentPositionCardView(
    view: InstrumentPositionCardView,
    item: PortfolioItem.InstrumentPositionCardItem?,
) {
    item?.let {
        view.setImage(item.position.instrument?.logo ?: "")
        view.setTitle(item.position.instrument?.symbol ?: "")
        view.setSubTitle(item.position.instrument?.name ?: "")
        view.setInvested(item.position.amount)
        view.setValueInvested(item.position.value)
        view.setUnit(item.position.units)
        view.setAvgOpen(item.position.openRate)
        view.setProfitLoss(item.position.profitLoss)
        view.setPrice(item.position.current)
        view.setChange(item.position.change)
        view.setChangePercent(item.position.changePercent)
        view.setStatusText("Market is ${item.position.status}")
    }
}

@BindingAdapter("setInsideAdapter", "activedColumn", "viewModel")
fun setInsidePositionAdapter(
    recycler: RecyclerView,
    items: MutableList<PortfolioItem>,
    activedColumn: List<String>?,
    viewModel: PositionViewModel?,
) {
    if (recycler.adapter == null) {
        recycler.apply {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = PortfolioAdapter().apply {
                onIntrumentClick = { position ->
                }
            }
        }
    }

    val adapter = recycler.adapter as PortfolioAdapter
    adapter.apply {
        columns = activedColumn ?: emptyList()
        itemLists = items
    }
}


@BindingAdapter("setCopierPosition", "column1", "column2", "column3", "column4")
fun setCopierPositionItem(
    view: PositionView,
    item: Copier?,
    column1: String?,
    column2: String?,
    column3: String?,
    column4: String?,
) {
    item?.let {
        view.setTextColumnOne(
            getCopierValueByColumn(
                item,
                column1 ?: ""
            )
        )
        view.setTextColumnTwo(
            getCopierValueByColumn(
                item,
                column2 ?: ""
            )
        )
        view.setTextColumnThree(
            getCopierValueByColumn(
                item,
                column3 ?: ""
            )
        )
        view.setTextColumnFour(
            getCopierValueByColumn(
                item,
                column4 ?: ""
            )
        )
        view.setTextColorColumnOne(
            getCopierColorByColumn(
                item,
                column1 ?: ""
            )
        )
        view.setTextColorColumnTwo(
            getCopierColorByColumn(
                item,
                column2 ?: ""
            )
        )
        view.setTextColorColumnThree(
            getCopierColorByColumn(
                item,
                column3 ?: ""
            )
        )
        view.setTextColorColumnFour(
            getCopierColorByColumn(
                item,
                column4 ?: ""
            )
        )
    }
}

@BindingAdapter("setPosition", "column1", "column2", "column3", "column4")
fun setPositionItem(
    view: PositionView,
    item: PortfolioItem.PositionItem?,
    column1: String?,
    column2: String?,
    column3: String?,
    column4: String?,
) {
    item?.let {
        view.setTextColumnOne(
            getPositionValueByColumn(
                item,
                column1 ?: ""
            )
        )
        view.setTextColumnTwo(
            getPositionValueByColumn(
                item,
                column2 ?: ""
            )
        )
        view.setTextColumnThree(
            getPositionValueByColumn(
                item,
                column3 ?: ""
            )
        )
        view.setTextColumnFour(
            getPositionValueByColumn(
                item,
                column4 ?: ""
            )
        )
        view.setTextColorColumnOne(
            getPositionColorColoumn(
                item,
                column1 ?: ""
            )
        )
        view.setTextColorColumnTwo(
            getPositionColorColoumn(
                item,
                column2 ?: ""
            )
        )
        view.setTextColorColumnThree(
            getPositionColorColoumn(
                item,
                column3 ?: ""
            )
        )
        view.setTextColorColumnFour(
            getPositionColorColoumn(
                item,
                column4 ?: ""
            )
        )
    }
}


private fun getPositionValueByColumn(
    item: PortfolioItem.PositionItem,
    column: String,
): String {
    return when (column) {
        "Invested" -> "$%.2f".format(item.invested)
        "Invested(%)" -> "%.2f%s".format(item.invested, "%")
        "Units" -> "%.2f".format(item.units)
        "Open", "Avg. Open" -> "%s".format(if (item.openRate != 0f) item.openRate else item.openRate)
        "Current" -> "%s".format(item.current)
        "P/L($)" -> "$%.2f".format(item.pl)
        "P/L(%)" -> "%.2f%s".format(
            item.plPercent,
            "%"
        )
        "Pip Change" -> "%s".format(item.pipChange)
        "Leverage" -> "%.2f".format(item.leverage)
        "Value" -> "$%.2f".format(item.value)
        "Value(%)" -> "%.2f%s".format(item.value, "%")
        "Fee" -> "$%.2f".format(item.fees)
        "SL" -> "%s".format(item.stopLoss)
        "TP" -> "%s".format(item.takeProfit)
        "SL($)" -> "$%.2f".format(item.amountStopLoss)
        "TP($)" -> "$%.2f".format(item.amountTakeProfit)
        "SL(%)" -> "%.2f%s".format(item.stopLossPercent, "%")
        "TP(%)" -> "%.2f%s".format(item.takeProfitPercent, "%")
        "Buy/Sell" -> "%s".format(item.buyOrSell)
        "Amount" -> "%.2f%s".format(item.invested, "%")
        else -> ""
    }
}

private fun getPositionColorColoumn(
    item: PortfolioItem.PositionItem,
    column: String,
): Int = when {
    column == "P/L($)" && item.pl < 0 -> R.color.awonar_color_orange
    column == "P/L($)" && item.pl >= 0 -> R.color.awonar_color_green
    column == "P/L(%)" && item.plPercent < 0 -> R.color.awonar_color_orange
    column == "P/L(%)" && item.plPercent >= 0 -> R.color.awonar_color_green
    column == "Pip Change" && item.pipChange < 0 -> R.color.awonar_color_orange
    column == "Pip Change" && item.pipChange >= 0 -> R.color.awonar_color_green
    else -> 0
}

private fun getPositionColorColoumn(
    item: PortfolioItem.InstrumentItem,
    column: String,
): Int = when {
    column == "P/L($)" && item.profitLoss < 0 -> R.color.awonar_color_orange
    column == "P/L($)" && item.profitLoss >= 0 -> R.color.awonar_color_green
    column == "P/L(%)" && item.profitLossPercent < 0 -> R.color.awonar_color_orange
    column == "P/L(%)" && item.profitLossPercent >= 0 -> R.color.awonar_color_green
    column == "Pip Change" && item.pipChange < 0 -> R.color.awonar_color_orange
    column == "Pip Change" && item.pipChange >= 0 -> R.color.awonar_color_green
    else -> 0
}

private fun getCopierValueByColumn(
    item: Copier,
    column: String,
): String = when (column) {
    "Invested" -> "$%.2f".format(item.invested)
    "Invested(%)" -> "%.2f%s".format(item.invested, "%")
    "P/L($)" -> "$%.2f".format(item.profitLoss)
    "P/L(%)" -> "%.2f%s".format(
        (if (item.netProfit != 0f) item.netProfit else item.profitLossPercent),
        "%"
    )
    "Value" -> "$%.2f".format(item.value)
    "Value(%)" -> "%.2f%s".format(item.value, "%")
    "Fee" -> "$%s".format(item.fees)
    "Net Invest" -> "$%.2f".format(item.netInvested)
    "CSL" -> "%s".format(item.copyStopLoss)
    "CSL(%)" -> "%.2f%s".format(item.copyStopLossPercent, "%")
    else -> ""
}

private fun getCopierColorByColumn(
    item: Copier,
    column: String,
): Int = when {
    column == "P/L($)" && item.profitLoss < 0 -> R.color.awonar_color_orange
    column == "P/L($)" && item.profitLoss >= 0 -> R.color.awonar_color_green
    column == "P/L(%)" && (if (item.netProfit != 0f) item.netProfit else item.profitLossPercent) < 0 -> R.color.awonar_color_orange
    column == "P/L(%)" && (if (item.netProfit != 0f) item.netProfit else item.profitLossPercent) >= 0 -> R.color.awonar_color_green
    else -> 0
}