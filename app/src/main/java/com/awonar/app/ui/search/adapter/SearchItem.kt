package com.awonar.app.ui.search.adapter

import android.os.Parcelable
import com.awonar.android.model.search.SearchData
import kotlinx.parcelize.Parcelize

sealed class SearchItem(val type: Int) : Parcelable {

    @Parcelize
    class Empty : SearchItem(SearchType.EMPTY)

    @Parcelize
    class LoadItem : SearchItem(SearchType.LOAD)

    @Parcelize
    class ListItem(val data: SearchData?, val isMarket: Boolean = true) :
        SearchItem(SearchType.ITEM)

    @Parcelize
    data class SectorItem(val text: String, val action: String = "") : SearchItem(SearchType.SECTOR)
}