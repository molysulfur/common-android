package com.awonar.app.domain.watchlist

import com.awonar.android.model.watchlist.WatchlistInfo
import com.awonar.android.shared.di.MainDispatcher
import com.awonar.app.ui.watchlist.adapter.WatchlistItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertWatchlistItemUseCase @Inject constructor(
    @MainDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<List<WatchlistInfo>, MutableList<WatchlistItem>>(dispatcher) {
    override suspend fun execute(parameters: List<WatchlistInfo>): MutableList<WatchlistItem> {
        val list = mutableListOf<WatchlistItem>()
        list.add(WatchlistItem.ColumnItem(
            "Market",
            "Sell",
            "Buy"
        ))
        parameters.filter { it.type == "instrument" }.forEach { info ->
            list.add(
                WatchlistItem.InstrumentItem(
                    id = info.id,
                    instrumentId = info.instrumentId,
                    title = info.title,
                    image = info.image
                )
            )
        }
        list.add(WatchlistItem.ColumnItem(
            "People",
            "Risk",
            "Change"
        ))
        parameters.filter { it.type == "user" }.forEach { info ->
            list.add(
                WatchlistItem.TraderItem(
                    id = info.id,
                    uid = info.uid,
                    title = info.title,
                    subTitle = info.subTitle,
                    image = info.image,
                    risk = info.risk,
                    gain = info.gain
                )
            )
        }
        return list
    }
}