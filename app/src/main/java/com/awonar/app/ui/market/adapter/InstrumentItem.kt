package com.awonar.app.ui.market.adapter

import android.os.Parcelable
import com.awonar.android.model.market.Instrument
import com.awonar.android.model.market.MarketViewMoreArg
import kotlinx.parcelize.Parcelize

sealed class InstrumentItem(val type: Int) : Parcelable {

    @Parcelize
    data class InstrumentCardItem(val instrument: Instrument) :
        InstrumentItem(InstrumentType.INSTRUMENT_CARD_TYPE)

    @Parcelize
    data class TitleItem(val title: String) :
        InstrumentItem(InstrumentType.INSTRUMENT_TITLE_TYPE)

    @Parcelize
    data class InstrumentListItem(
        val instrument: Instrument,
        var bid: Float = 0f,
        var ask: Float = 0f
    ) :
        InstrumentItem(InstrumentType.INSTRUMENT_LIST_ITEM_TYPE)

    @Parcelize
    data class InstrumentViewMoreItem(val arg: MarketViewMoreArg) :
        InstrumentItem(InstrumentType.INSTRUMENT_VIEW_MORE_TYPE)

    @Parcelize
    class LoadingItem : InstrumentItem(InstrumentType.INSTRUMENT_LOADING_TYPE)

    @Parcelize
    class BlankItem : InstrumentItem(InstrumentType.INSTRUMENT_BLANK_TYPE)

    @Parcelize
    class DividerItem : InstrumentItem(InstrumentType.INSTRUMENT_DIVIDER_TYPE)

}