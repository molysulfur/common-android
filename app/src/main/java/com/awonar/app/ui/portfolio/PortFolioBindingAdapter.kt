package com.awonar.app.ui.portfolio

import android.annotation.SuppressLint
import android.app.Activity
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
import com.awonar.app.ui.portfolio.activedadapter.ActivedColumnAdapter
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioAdapter
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.awonar.app.utils.DateUtils
import com.awonar.app.widget.CopierPositionCardView
import com.awonar.app.widget.InstrumentOrderView
import com.awonar.app.widget.InstrumentPositionCardView
import com.google.android.material.appbar.MaterialToolbar
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

@BindingAdapter("quotes")
fun updateQuoteList(
    recycler: RecyclerView,
    quote: Array<Quote>
) {
    if (recycler.adapter != null) {
        val adapter = recycler.adapter as OrderPortfolioAdapter
        adapter.quote = quote
    }
}


@BindingAdapter("setOrderAdapter", "activedColumn", "viewModel")
fun setOrderAdapter(
    recycler: RecyclerView,
    items: MutableList<OrderPortfolioItem>,
    activedColumn: List<String> = emptyList(),
    viewModel: PortFolioViewModel
) {
    if (recycler.adapter == null) {
        recycler.apply {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = OrderPortfolioAdapter().apply {
                onClick = { it, type ->
                }
            }
        }
        val callback = PortfolioListItemTouchHelperCallback(recycler.context)
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(recycler)
        recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)
                callback.onDraw(c)
            }
        })
    }
    val adapter = recycler.adapter as OrderPortfolioAdapter
    adapter.columns = activedColumn
    adapter.itemLists = items
}

@BindingAdapter("setTotalInvested")
fun setTotalInvested(
    textView: TextView,
    copier: Copier?,
) {
    copier?.let {
        textView.text = "$%.2f".format(it.investAmount)
    }
}

@BindingAdapter("setTotalProfitLossPercent", "quotes")
fun setTotalProfitLossPercent(
    textView: TextView,
    copier: Copier?,
    quotes: Array<Quote>
) {
    copier?.let { copier ->
        var sumFloatingPL = PortfolioUtil.getFloatingPL(copier.positions ?: emptyList(), quotes)
        textView.text = "$%.2f".format(
            copier.closedPositionsNetProfit.plus(sumFloatingPL).div(copier.initialInvestment)
                .times(100)
        )
    }
}

@BindingAdapter("setTotalProfitLoss", "quotes")
fun setTotalProfitLoss(
    textView: TextView,
    copier: Copier?,
    quotes: Array<Quote>
) {
    copier?.let { copier ->
        var sumFloatingPL = PortfolioUtil.getFloatingPL(copier.positions ?: emptyList(), quotes)
        textView.text = "$%.2f".format(copier.closedPositionsNetProfit.plus(sumFloatingPL))
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setTotalOpen", "quotes")
fun setTotalOpen(
    textView: TextView,
    copier: Copier?,
    quotes: Array<Quote>
) {
    copier?.let {
        textView.text = "fee : ${it.totalFees} pl : ${it.closedPositionsNetProfit}"
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setTotalClose")
fun setTotalClose(
    textView: TextView,
    copier: Copier?
) {
    copier?.let {
        textView.text = "fee : $%.2f pl : $%.2f".format(it.totalFees, it.closedPositionsNetProfit)
    }
}

@BindingAdapter("initCopierCard")
fun initCopierCard(
    view: CopierPositionCardView,
    item: OrderPortfolioItem.CopierPositionCardItem?
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
    item: OrderPortfolioItem.InstrumentPositionCardItem?
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
    item: OrderPortfolioItem.InstrumentPositionCardItem?,
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
    items: MutableList<OrderPortfolioItem>
) {
    if (recycler.adapter == null) {
        recycler.apply {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = OrderPortfolioAdapter().apply {
                onClick = { it, type ->
                }
            }
        }
        val callback = PortfolioListItemTouchHelperCallback(recycler.context)
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(recycler)
        recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)
                callback.onDraw(c)
            }
        })
    }
    (recycler.adapter as OrderPortfolioAdapter).itemLists = items
}

@BindingAdapter("copier", "conversionRateList", "quote")
fun updateCopierProfitLoss(
    view: CopierPositionCardView,
    copier: Copier?,
    conversions: HashMap<Int, Float>,
    quotes: Array<Quote>?
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
    copier: Copier?
) {
    copier?.let {
        val money = it.depositSummary.minus(it.withdrawalSummary)
        val value = it.initialInvestment.plus(money)
        view.apply {
            setImage(it.user.picture ?: "")
            setTitle("${it.user.firstName} ${it.user.middleName} ${it.user.lastName}")
            setDescrption(it.user.username ?: "")
            setInvested(it.investAmount)
            setMoney(money)
            setValueInvested(value)
        }
    }
}

@BindingAdapter("setInstrumentPositionCard")
fun setInstrumentPositionCard(
    view: InstrumentPositionCardView,
    position: Position?
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
    quotes: Array<Quote>
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

@BindingAdapter("setPositionAdapter", "activedColumn", "viewModel")
fun setPositionAdapter(
    recycler: RecyclerView,
    items: MutableList<OrderPortfolioItem>,
    activedColumn: List<String> = emptyList(),
    viewModel: PortFolioViewModel
) {
    if (recycler.adapter == null) {
        recycler.apply {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = OrderPortfolioAdapter().apply {
                onClick = { it, type ->
                    viewModel.navigateInsidePortfolio(it, type)
                }
                onButtonClick = { text ->
                    when (text.lowercase()) {
                        "allocate" -> {
                            this.pieChartType = "allocate"
                            viewModel.getAllocate()
                        }
                        "exposure" -> {
                            this.pieChartType = "exposure"
                            viewModel.getExposure()
                        }
                    }
                }
                onPieChartClick = {
                    when (this.pieChartType) {
                        "allocate" -> viewModel.getAllocate(it)
                        "exposure" -> viewModel.getExposure(it)
                    }

                }
            }
        }
        val callback = PortfolioListItemTouchHelperCallback(recycler.context)
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(recycler)
        recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)
                callback.onDraw(c)
            }
        })
    }
    val adapter = recycler.adapter as OrderPortfolioAdapter
    adapter.apply {
        columns = activedColumn
        itemLists = items
    }
}

@BindingAdapter("setActivedColumn", "viewModel")
fun setActivedColumn(
    recycler: RecyclerView,
    activedList: List<String>,
    viewModel: PortFolioViewModel
) {
    if (recycler.adapter == null) {
        recycler.apply {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = ActivedColumnAdapter().apply {
                onClick = { text ->
                    viewModel.activedColumnChange(text)
                }
            }
        }
    }
    (recycler.adapter as ActivedColumnAdapter).itemList = activedList
}

@BindingAdapter("setActivedColumnToolbar")
fun setActivedColumnToolbar(toolbar: MaterialToolbar, viewModel: PortFolioViewModel) {
    toolbar.setNavigationOnClickListener {
        (toolbar.context as Activity).finish()
    }
    toolbar.setOnMenuItemClickListener {
        when (it.itemId) {
            R.id.awonar_toolbar_actived_column_save -> viewModel.saveActivedColumn()
            R.id.awonar_toolbar_actived_column_reset -> viewModel.resetActivedColumn()
        }
        (toolbar.context as Activity).run {
            setResult(Activity.RESULT_OK)
            finish()
        }
        false
    }
}

@BindingAdapter("setPositionOrder", "column1", "column2", "column3", "column4")
fun setItemPositionOrderPortfolio(
    view: InstrumentOrderView,
    item: OrderPortfolioItem?,
    column1: String?,
    column2: String?,
    column3: String?,
    column4: String?,
) {
    when (item) {
        is OrderPortfolioItem.InstrumentOrderItem -> item.position.let { position ->
            view.setImage(position.instrument.logo ?: "")
            view.setTitle("${if (position.isBuy) "BUY" else "SELL"} ${position.instrument.symbol}")
            view.setDescription(DateUtils.getDate(position.openDateTime))
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
        is OrderPortfolioItem.InstrumentPortfolioItem -> item.position.let { position ->
            view.setImage(position.instrument.logo ?: "")
            view.setTitle("${if (position.isBuy) "BUY" else "SELL"} ${position.instrument.symbol}")
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
            val date = parser.parse(position.openDateTime)
            date?.let {
                view.setDescription(formatter.format(date))
            }
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
        is OrderPortfolioItem.CopierPortfolioItem -> item.copier.let { copy ->
            view.setImage(copy.user.picture ?: "")
            view.setTitle(copy.user.username ?: "")
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
    item: OrderPortfolioItem.CopierPortfolioItem,
    column: String
): Int = when {
    column == "P/L($)" && item.profitLoss < 0 -> R.color.awonar_color_orange
    column == "P/L($)" && item.profitLoss >= 0 -> R.color.awonar_color_green
    column == "P/L(%)" && item.profitLossPercent < 0 -> R.color.awonar_color_orange
    column == "P/L(%)" && item.profitLossPercent >= 0 -> R.color.awonar_color_green
    else -> 0
}

private fun getCopierValueByColumn(
    item: OrderPortfolioItem.CopierPortfolioItem,
    column: String
): String = when (column) {
    "Invested" -> "$%.2f".format(item.invested)
    "P/L($)" -> "$%.2f".format(item.profitLoss)
    "P/L(%)" -> "%s%s".format(item.profitLossPercent, "%")
    "Value" -> "$%s".format(item.value)
    "Fee" -> "$%s".format(item.fees)
    "Net Invest" -> "$%.2f".format(item.netInvested)
    "CSL" -> "%s".format(item.copyStopLoss)
    "CSL(%)" -> "%.2f%s".format(item.copyStopLossPercent, "%")
    else -> ""
}

private fun getPositionColorColoumn(
    item: OrderPortfolioItem.InstrumentPortfolioItem,
    column: String
): Int = when {
    column == "P/L($)" && item.profitLoss < 0 -> R.color.awonar_color_orange
    column == "P/L($)" && item.profitLoss >= 0 -> R.color.awonar_color_green
    column == "P/L(%)" && item.profitLossPercent < 0 -> R.color.awonar_color_orange
    column == "P/L(%)" && item.profitLossPercent >= 0 -> R.color.awonar_color_green
    column == "Pip Change" && item.pipChange < 0 -> R.color.awonar_color_orange
    column == "Pip Change" && item.pipChange >= 0 -> R.color.awonar_color_green
    else -> 0
}

private fun getPositionColorColoumn(
    item: OrderPortfolioItem.InstrumentOrderItem,
    column: String
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
    item: OrderPortfolioItem.InstrumentPortfolioItem,
    column: String
): String = when (column) {
    "Invested" -> "$%.2f".format(item.invested)
    "Units" -> "%.2f".format(item.units)
    "Open" -> "%s".format(item.open)
    "Current" -> "%s".format(item.current)
    "P/L($)" -> "$%.2f".format(item.profitLoss)
    "P/L(%)" -> "%.2f%s".format(item.profitLossPercent, "%")
    "Pip Change" -> "%s".format(item.pipChange.toInt())
    "Leverage" -> "%s".format(item.leverage.toFloat())
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

private fun getPositionValueByColumn(
    item: OrderPortfolioItem.InstrumentOrderItem,
    column: String
): String = when (column) {
    "Invested" -> "$%.2f".format(item.invested)
    "Units" -> "%.2f".format(item.units)
    "Open" -> "%s".format(item.open)
    "Current" -> "%s".format(item.current)
    "P/L($)" -> "$%.2f".format(item.profitLoss)
    "P/L(%)" -> "%.2f%s".format(item.profitLossPercent, "%")
    "Pip Change" -> "%s".format(item.pipChange.toInt())
    "Leverage" -> "%s".format(item.leverage.toFloat())
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
