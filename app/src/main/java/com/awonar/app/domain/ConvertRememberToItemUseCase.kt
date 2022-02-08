package com.awonar.app.domain

import com.awonar.android.model.market.Instrument
import com.awonar.android.model.market.MarketViewMoreArg
import com.awonar.android.shared.di.MainDispatcher
import com.awonar.app.ui.market.adapter.InstrumentItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertRememberToItemUseCase @Inject constructor(
    @MainDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<List<Instrument>, List<InstrumentItem>>(dispatcher) {

    companion object {
        private val RECOMMENDED_LIMIT = 5
    }

    override suspend fun execute(instruments: List<Instrument>): List<InstrumentItem> {
        val itemList = arrayListOf<InstrumentItem>()
        itemList.add(InstrumentItem.TitleItem("Stocks"))
        takeInstrumentWithCategory(instruments, "stocks").forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(InstrumentItem.InstrumentViewMoreItem(MarketViewMoreArg("stocks", "category")))
        itemList.add(InstrumentItem.TitleItem("Currencies"))
        takeInstrumentWithCategory(instruments, "currencies").forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(
            InstrumentItem.InstrumentViewMoreItem(
                MarketViewMoreArg(
                    "currencies",
                    "category"
                )
            )
        )
        itemList.add(InstrumentItem.TitleItem("Crypto"))
        takeInstrumentWithCategory(instruments, "crypto").forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(InstrumentItem.InstrumentViewMoreItem(MarketViewMoreArg("crypto", "category")))
        itemList.add(InstrumentItem.TitleItem("Technology"))
        takeInstrumentWithSector(instruments, "technology").forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(
            InstrumentItem.InstrumentViewMoreItem(
                MarketViewMoreArg(
                    "technology",
                    "sector"
                )
            )
        )
        itemList.add(InstrumentItem.TitleItem("Healthcare"))
        takeInstrumentWithSector(instruments, "healthcare").forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(
            InstrumentItem.InstrumentViewMoreItem(
                MarketViewMoreArg(
                    "healthcare",
                    "sector"
                )
            )
        )
        itemList.add(InstrumentItem.TitleItem("Financial Service"))
        takeInstrumentWithSector(
            instruments,
            "financial-services"
        ).forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(
            InstrumentItem.InstrumentViewMoreItem(
                MarketViewMoreArg(
                    "financial-services",
                    "sector"
                )
            )
        )
        return itemList
    }

    private fun addInstrument(
        itemList: ArrayList<InstrumentItem>,
        index: Int,
        instrument: Instrument,
    ) {
        if (index > 0) {
            itemList.add(InstrumentItem.DividerItem())
        }
        itemList.add(InstrumentItem.InstrumentListItem(instrument))
    }

    private fun takeInstrumentWithCategory(
        instruments: List<Instrument>,
        category: String,
    ): List<Instrument> =
        instruments.filter { val contains = it.categories.contains(category)
            contains
        }.take(RECOMMENDED_LIMIT)

    private fun takeInstrumentWithSector(
        instruments: List<Instrument>,
        sector: String,
    ): List<Instrument> =
        instruments.filter { it.sector?.contains(sector) == true }.take(RECOMMENDED_LIMIT)
}