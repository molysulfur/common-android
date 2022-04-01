package com.awonar.app.ui.profile.stat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.*
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_BLANK
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_BUTTON
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_BUTTON_GROUP
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_CHART_LINE
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_CHART_PIE
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_CHART_POSITIVE_NEGATIVE
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_CHART_STACKED
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_DIVIDER
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_LINEAR_COLORS
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_LIST_ITEM
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_RISK
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_SELECTOR
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_TEXT_BOX
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_TOTAL_GAIN
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_TOTAL_TRADE
import com.awonar.app.ui.profile.stat.holder.*

class StatisticAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList = mutableListOf<StatisticItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    var onClick: ((String?) -> Unit)? = null
    var onSelected: ((String?) -> Unit)? = null
    var onChecked: ((Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            StatisticType.STATISTIC_TITLE -> TitleViewHolder(
                AwonarItemCenterTitleBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            StatisticType.STATISTIC_SECTION -> SectionViewHolder(
                AwonarItemSectionBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            STATISTIC_LINEAR_COLORS -> HorizontalColorsViewHolder(
                AwonarItemLinearViewBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            STATISTIC_TOTAL_TRADE -> TotalTradeViewHolder(
                AwonarItemTotalTradeBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            STATISTIC_DIVIDER -> DividerViewHolder(
                AwonarItemDividerBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            STATISTIC_LIST_ITEM -> ListItemViewHolder(
                AwonarItemMarkerListItemBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            STATISTIC_CHART_PIE -> PiechartViewHolder(
                AwonarItemPiechartBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            STATISTIC_CHART_LINE -> GrowthDayViewHolder(
                AwonarItemLineChartBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            STATISTIC_TEXT_BOX -> TextBoxViewHolder(
                AwonarItemTextBoxBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            STATISTIC_RISK -> RiskViewHolder(
                AwonarItemListBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            STATISTIC_BUTTON -> ButtonViewHolder(
                AwonarItemButtonViewmoreBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            STATISTIC_TOTAL_GAIN -> TotalGainViewHolder(
                AwonarItemListBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            STATISTIC_CHART_POSITIVE_NEGATIVE -> PositveNegativeChartViewHolder(
                AwonarItemBarchartBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            STATISTIC_CHART_STACKED -> StackedChartViewHolder(
                AwonarItemStackedChartBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )

            STATISTIC_BUTTON_GROUP -> ButtonGroupViewHolder(
                AwonarItemButtonGroupBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            STATISTIC_SELECTOR -> SelectorViewHolder(
                AwonarItemSelectorBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            STATISTIC_BLANK -> BlankViewHolder(
                AwonarItemBlankGrayBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            else -> throw Error("View Type is not found!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is SectionViewHolder -> holder.bind(item as StatisticItem.SectionItem)
            is TitleViewHolder -> holder.bind(item as StatisticItem.TitleItem)
            is HorizontalColorsViewHolder -> holder.bind(item as StatisticItem.LinearColorsItem)
            is TotalTradeViewHolder -> holder.bind(item as StatisticItem.TotalTradeItem)
            is ListItemViewHolder -> holder.bind(item as StatisticItem.ListItem)
            is PiechartViewHolder -> holder.bind(item as StatisticItem.PieChartItem)
            is GrowthDayViewHolder -> holder.bind(item as StatisticItem.GrowthDayItem)
            is TextBoxViewHolder -> holder.bind(item as StatisticItem.TextBoxItem)
            is RiskViewHolder -> holder.bind(item as StatisticItem.RiskItem)
            is StackedChartViewHolder -> holder.bind(item as StatisticItem.StackedChartItem)
            is ButtonViewHolder -> holder.bind(item as StatisticItem.ButtonItem, onClick)
            is TotalGainViewHolder -> holder.bind(item as StatisticItem.TotalGainItem)
            is ButtonGroupViewHolder -> holder.bind(item as StatisticItem.ButtonGroupItem,
                onChecked)
            is SelectorViewHolder -> holder.bind(item as StatisticItem.SelectorItem, onSelected)
            is PositveNegativeChartViewHolder -> holder.bind(item as StatisticItem.PositiveNegativeChartItem)
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int = itemList[position].type

    class StatisticalDiffCallback(
        private val oldItems: MutableList<StatisticItem>?,
        private val newItems: MutableList<StatisticItem>?,
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems?.size ?: 0

        override fun getNewListSize(): Int = newItems?.size ?: 0

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems?.get(oldItemPosition) === newItems?.get(newItemPosition)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems?.get(oldItemPosition) == newItems?.get(newItemPosition)
        }
    }

}