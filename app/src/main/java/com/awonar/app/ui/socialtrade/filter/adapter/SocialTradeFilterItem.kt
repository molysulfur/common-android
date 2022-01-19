package com.awonar.app.ui.socialtrade.filter.adapter

import android.os.Parcelable
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterType.LIST_SELECTOR_TYPE
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterType.LIST_TEXT_TYPE
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterType.SECTION_TYPE
import kotlinx.parcelize.Parcelize

sealed class SocialTradeFilterItem(
    val type: Int,
) : Parcelable {
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
    class SelectorListItem(
        val text: String,
        val icon: String? = null,
        val iconRes: Int = 0,
        val isChecked: Boolean = false,
    ) : SocialTradeFilterItem(LIST_SELECTOR_TYPE)

    @Parcelize
    class SectionItem(
        val text: String,
    ) : SocialTradeFilterItem(SECTION_TYPE)
}