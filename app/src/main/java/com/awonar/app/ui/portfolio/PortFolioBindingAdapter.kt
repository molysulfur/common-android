package com.awonar.app.ui.portfolio

import android.graphics.Canvas
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.android.model.portfolio.Copier
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.android.shared.utils.PortfolioUtil
import com.awonar.app.R
import com.awonar.app.ui.portfolio.adapter.IPortfolioListItemTouchHelperCallback
import com.awonar.app.ui.portfolio.adapter.PortfolioAdapter
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.awonar.app.ui.portfolio.adapter.PortfolioListItemTouchHelperCallback
import com.awonar.app.ui.portfolio.position.PositionViewModel
import com.awonar.app.utils.ColorChangingUtil
import com.awonar.app.widget.CopierPositionCardView
import com.awonar.app.widget.PositionView
import com.awonar.app.widget.InstrumentPositionCardView
import kotlin.collections.HashMap

@BindingAdapter("setUserPortfolio", "column1", "column2", "column3", "column4")
fun setUserPortfolio(
    recycler: RecyclerView,
    itemList: MutableList<PortfolioItem>,
    column1: String?,
    column2: String?,
    column3: String?,
    column4: String?,
) {
    if (recycler.adapter == null) {
        with(recycler) {
            adapter = PortfolioAdapter()
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
        setTextColor(ColorChangingUtil.getTextColorChange(
            textView.context,
            profit))
        text = "Profit: $%.2f".format(profit)
    }
}

@BindingAdapter("setEquity")
fun setEquity(
    textView: TextView,
    equity: Float,
) {
    with(textView) {
        setTextColor(ColorChangingUtil.getTextColorChange(
            textView.context,
            equity))
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
        view.setImage(item.position.instrument.logo ?: "")
        view.setTitle(item.position.instrument.symbol ?: "")
        view.setSubTitle(item.position.instrument.name ?: "")
        view.setInvested(item.invested)
        view.setValueInvested(item.value)
        view.setUnit(item.units)
        view.setAvgOpen(item.avgOpen)
        view.setProfitLoss(item.profitLoss)
    }
}


@BindingAdapter("instrumentPositionCardItem", "quote")
fun setInsturmentPositionCardWithQuote(
    view: InstrumentPositionCardView,
    item: PortfolioItem.InstrumentPositionCardItem?,
    quote: Quote?,
) {
    quote?.let {
        val current = PortfolioUtil.getCurrent(item?.position?.isBuy == true, it)
        val profitLoss =
            PortfolioUtil.getProfitOrLoss(
                current,
                item?.position?.openRate ?: 0f,
                item?.position?.units ?: 0f,
                item?.conversion ?: 1f,
                item?.position?.isBuy == true
            )
        view.setPrice(current)
        view.setChange(ConverterQuoteUtil.change(quote.close, quote.previous))
        view.setChangePercent(ConverterQuoteUtil.percentChange(quote.previous, quote.close))
        view.setStatusText("${quote.status}")
        view.setProfitLoss(profitLoss)
    }
}

@BindingAdapter("setItemListCard")
fun setPositionCardAdapter(
    recycler: RecyclerView,
    items: MutableList<PortfolioItem>,
) {
    if (recycler.adapter == null) {
        recycler.apply {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = PortfolioAdapter().apply {
                onClick = { it, type ->
                }
            }
        }
        val callback = PortfolioListItemTouchHelperCallback(
            object : IPortfolioListItemTouchHelperCallback {
                override fun onClick(position: Int) {

                }

                override fun onClose(position: Int) {
                }
            },
            recycler.context
        )
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(recycler)
        recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)
                callback.onDraw(c)
            }
        })
    }
    (recycler.adapter as PortfolioAdapter).itemLists = items
}

@BindingAdapter("copier", "conversionRateList", "quote")
fun updateCopierProfitLoss(
    view: CopierPositionCardView,
    copier: Copier?,
    conversions: HashMap<Int, Float>,
    quotes: Array<Quote>?,
) {
    copier?.positions?.forEach { position ->
        val quote = quotes?.find { it.id == position.instrumentId }
        var sumFloatingPL = 0f
        quote?.let {
            val current = if (position.isBuy) it.bid else it.ask
            val pl = PortfolioUtil.getProfitOrLoss(
                current,
                position.openRate,
                position.units,
                conversions[position.instrumentId] ?: 1f,
                position.isBuy
            )
            sumFloatingPL = sumFloatingPL.plus(pl)
        }
        view.setProfitLoss(copier.closedPositionsNetProfit.plus(sumFloatingPL))
    }
}

@BindingAdapter("setOrder")
fun setCopierPositionCard(
    view: CopierPositionCardView,
    copier: Copier?,
) {
    copier?.let {
        val money = it.depositSummary.minus(it.withdrawalSummary)
        val value = it.initialInvestment.plus(money)
        view.apply {
            setImage(it.user.picture ?: "")
            setTitle("${it.user.firstName} ${it.user.middleName} ${it.user.lastName}")
            setDescrption(it.user.username ?: "")
            setInvested(it.initialInvestment)
            setMoney(money)
            setValueInvested(value)
        }
    }
}

@BindingAdapter("setInstrumentPositionCard")
fun setInstrumentPositionCard(
    view: InstrumentPositionCardView,
    position: Position?,
) {
    position?.let {
        view.apply {
            setImage(it.instrument.logo ?: "")
            setTitle(it.instrument.symbol ?: "")
            setSubTitle(it.instrument.name ?: "")
            setDescrption(it.instrument.industry ?: "")
            setInvested(it.amount)
            setUnit(it.units)
            setAvgOpen(it.openRate)
        }
    }
}

@BindingAdapter("position", "isBuy", "conversionRate", "quotes")
fun updateQuoteInstrumentPosition(
    view: InstrumentPositionCardView,
    position: Position?,
    isBuy: Boolean?,
    conversionRate: Float,
    quotes: Array<Quote>,
) {
    val quote = quotes.find { it.id == position?.instrumentId }
    quote?.let {
        val current = if (isBuy == true) it.bid else it.ask
        val profitLoss = PortfolioUtil.getProfitOrLoss(
            current = current,
            openRate = position?.openRate ?: 0f,
            unit = position?.units ?: 0f,
            rate = conversionRate,
            isBuy = isBuy == true
        )
        val valueInvest = PortfolioUtil.getValue(profitLoss, position?.amount ?: 0f)
        view.apply {
            setPrice(current)
            setProfitLoss(profitLoss)
            setChange(ConverterQuoteUtil.change(it.close, it.previous))
            setStatusText("${it.status ?: "Closed"}")
            setValueInvested(valueInvest)
            setChangePercent(ConverterQuoteUtil.percentChange(it.previous, it.close))
        }
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
                onClick = { index, type ->
                    viewModel?.navigateInstrumentInside(index, "copies_instrument")
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

@BindingAdapter("setPositionAdapter", "activedColumn", "viewModel")
fun setPositionAdapter(
    recycler: RecyclerView,
    items: MutableList<PortfolioItem>,
    activedColumn: List<String> = emptyList(),
    viewModel: PositionViewModel,
) {
    if (recycler.adapter == null) {
        recycler.apply {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = PortfolioAdapter().apply {
                onClick = { it, type ->
                    viewModel.navigateInstrumentInside(it, type)
                }
                onViewAllClick = {
//                    viewModel.togglePortfolio("market")
                }
                onButtonClick = { text ->
                    when (text.lowercase()) {
                        "allocate" -> {
                            this.pieChartType = "allocate"
//                            viewModel.getAllocate()
                        }
                        "exposure" -> {
                            this.pieChartType = "exposure"
//                            viewModel.getExposure()
                        }
                    }
                }
                onPieChartClick = {
                    when (this.pieChartType) {
//                        "allocate" -> viewModel.getAllocate(it)
//                        "exposure" -> viewModel.getExposure(it)
                    }

                }
            }
        }
    }
    val adapter = recycler.adapter as PortfolioAdapter
    adapter.apply {
        columns = activedColumn
        itemLists = items
    }
}

@BindingAdapter("setPositionOrder", "column1", "column2", "column3", "column4")
fun setItemPositionOrderPortfolio(
    view: PositionView,
    item: PortfolioItem?,
    column1: String?,
    column2: String?,
    column3: String?,
    column4: String?,
) {
    when (item) {
        is PortfolioItem.InstrumentItem -> item.position.let { position ->
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
        is PortfolioItem.InstrumentPortfolioItem -> item.position.let { position ->
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
        is PortfolioItem.CopierPortfolioItem -> item.copier.let { copy ->
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
        else -> {
        }
    }
}

private fun getCopierColorByColumn(
    item: PortfolioItem.CopierPortfolioItem,
    column: String,
): Int = when {
    column == "P/L($)" && item.profitLoss < 0 -> R.color.awonar_color_orange
    column == "P/L($)" && item.profitLoss >= 0 -> R.color.awonar_color_green
    column == "P/L(%)" && item.profitLossPercent < 0 -> R.color.awonar_color_orange
    column == "P/L(%)" && item.profitLossPercent >= 0 -> R.color.awonar_color_green
    else -> 0
}

private fun getCopierValueByColumn(
    item: PortfolioItem.CopierPortfolioItem,
    column: String,
): String = when (column) {
    "Invested" -> "$%.2f".format(item.invested)
    "P/L($)" -> "$%.2f".format(item.profitLoss)
    "P/L(%)" -> "%.2f%s".format(item.profitLossPercent, "%")
    "Value" -> "$%s".format(item.value)
    "Fee" -> "$%s".format(item.fees)
    "Net Invest" -> "$%.2f".format(item.netInvested)
    "CSL" -> "%s".format(item.copyStopLoss)
    "CSL(%)" -> "%.2f%s".format(item.copyStopLossPercent, "%")
    else -> ""
}

private fun getPositionColorColoumn(
    item: PortfolioItem.InstrumentPortfolioItem,
    column: String,
): Int = when {
    column == "P/L($)" && item.position.profitLoss < 0 -> R.color.awonar_color_orange
    column == "P/L($)" && item.position.profitLoss >= 0 -> R.color.awonar_color_green
    column == "P/L(%)" && item.position.profitLossPercent < 0 -> R.color.awonar_color_orange
    column == "P/L(%)" && item.position.profitLossPercent >= 0 -> R.color.awonar_color_green
    column == "Pip Change" && item.position.pipChange < 0 -> R.color.awonar_color_orange
    column == "Pip Change" && item.position.pipChange >= 0 -> R.color.awonar_color_green
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

private fun getPositionValueByColumn(
    item: PortfolioItem.InstrumentPortfolioItem,
    column: String,
): String = when (column) {
    "Invested" -> "$%.2f".format(item.position.invested)
    "Units" -> "%.2f".format(item.position.units)
    "Open", "Avg. Open" -> "%s".format(item.position.open)
    "Current" -> "%s".format(item.position.current)
    "P/L($)" -> "$%.2f".format(item.position.profitLoss)
    "P/L(%)" -> "%.2f%s".format(item.position.profitLossPercent, "%")
    "Pip Change" -> "%s".format(item.position.pipChange.toInt())
    "Leverage" -> "%s".format(item.position.leverage)
    "Value" -> "$%.2f".format(item.position.value)
    "Fee" -> "$%.2f".format(item.position.fees)
    "Execute at" -> "$%s".format(item.position.invested)
    "SL" -> "%s".format(item.position.stopLoss)
    "TP" -> "%s".format(item.position.takeProfit)
    "SL($)" -> "$%.2f".format(item.position.amountStopLoss)
    "TP($)" -> "$%.2f".format(item.position.amountTakeProfit)
    "SL(%)" -> "%.2f%s".format(item.position.stopLossPercent, "%")
    "TP(%)" -> "%.2f%s".format(item.position.takeProfitPercent, "%")
    else -> ""
}

private fun getPositionValueByColumn(
    item: PortfolioItem.InstrumentItem,
    column: String,
): String = when (column) {
    "Invested" -> "$%.2f".format(item.invested)
    "Units" -> "%.2f".format(item.units)
    "Open" -> "%s".format(item.open)
    "Current" -> "%s".format(item.current)
    "P/L($)" -> "$%.2f".format(item.profitLoss)
    "P/L(%)" -> "%.2f%s".format(item.profitLossPercent, "%")
    "Pip Change" -> "%s".format(item.pipChange.toInt())
    "Leverage" -> "%s".format(item.leverage)
    "Value" -> "$%.2f".format(item.value)
    "Fee" -> "$%.2f".format(item.fees)
    "Execute at" -> "$%s".format(item.invested)
    "SL" -> "%s".format(item.stopLoss)
    "TP" -> "%s".format(item.takeProfit)
    "SL($)" -> "$%.2f".format(item.amountStopLoss)
    "TP($)" -> "$%.2f".format(item.amountTakeProfit)
    "SL(%)" -> "%.2f%s".format(item.stopLossPercent, "%")
    "TP(%)" -> "%.2f%s".format(item.takeProfitPercent, "%")
    else -> ""
}