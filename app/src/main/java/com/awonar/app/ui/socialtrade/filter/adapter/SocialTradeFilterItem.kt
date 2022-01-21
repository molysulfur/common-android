package com.awonar.app.ui.socialtrade.filter.adapter

import android.os.Parcelable
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterType.DESCRIPTION_TYPE
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterType.LIST_MULTI_SELECTOR_TYPE
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterType.LIST_SINGLE_SELECTOR_TYPE
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterType.LIST_TEXT_TYPE
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterType.RANGE_INPUT_TYPE
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterType.SECTION_TYPE
import kotlinx.parcelize.Parcelize

sealed class SocialTradeFilterItem(
    val type: Int,
) : Parcelable {

    @Parcelize
    class RangeInputItem : SocialTradeFilterItem(RANGE_INPUT_TYPE)

    @Parcelize
    data class DescriptionItem(
        val text: String?,
    ) : SocialTradeFilterItem(DESCRIPTION_TYPE)

    @Parcelize
    class TextListItem(
        val key: String?,
        val text: String,
        val icon: String? = null,
        val iconRes: Int = 0,
        val meta: String? = null,
        val metaRes: Int = 0,
    ) : SocialTradeFilterItem(LIST_TEXT_TYPE)

    @Parcelize
    class MultiSelectorListItem(
        val text: String,
        val icon: String? = null,
        val iconRes: Int = 0,
        var isChecked: Boolean = false,
    ) : SocialTradeFilterItem(LIST_MULTI_SELECTOR_TYPE)

    @Parcelize
    class SingleSelectorListItem(
        val text: String,
        val icon: String? = null,
        val iconRes: Int = 0,
        var isChecked: Boolean = false,
    ) : SocialTradeFilterItem(LIST_SINGLE_SELECTOR_TYPE)

    @Parcelize
    class SectionItem(
        val text: String,
    ) : SocialTradeFilterItem(SECTION_TYPE)
}