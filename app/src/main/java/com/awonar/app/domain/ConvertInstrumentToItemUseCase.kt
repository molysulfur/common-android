package com.awonar.app.domain

import com.awonar.android.model.market.Instrument
import com.awonar.android.shared.di.MainDispatcher
import com.awonar.app.ui.market.adapter.InstrumentItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertInstrumentToItemUseCase @Inject constructor(
    @MainDispatcher dispatcher: CoroutineDispatcher
) : UseCase<List<Instrument>, List<InstrumentItem>>(dispatcher) {

    companion object {
        private val RECOMMENDED_LIMIT = 5
    }

    override suspend fun execute(instruments: List<Instrument>): List<InstrumentItem> {
        val itemList = arrayListOf<InstrumentItem>()
        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.TitleItem("Stocks"))
        itemList.add(InstrumentItem.BlankItem())
        takeInstrumentWithCategory(instruments, "stocks").forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(InstrumentItem.InstrumentViewMoreItem("Stocks"))
        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.TitleItem("Currencies"))
        itemList.add(InstrumentItem.BlankItem())
        takeInstrumentWithCategory(instruments, "currencies").forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(InstrumentItem.InstrumentViewMoreItem("Currencies"))
        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.TitleItem("Crypto"))
        itemList.add(InstrumentItem.BlankItem())
        takeInstrumentWithCategory(instruments, "crypto").forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(InstrumentItem.InstrumentViewMoreItem("Crypto"))
        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.TitleItem("Technology"))
        itemList.add(InstrumentItem.BlankItem())
        takeInstrumentWithSector(instruments, "technology").forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(InstrumentItem.InstrumentViewMoreItem("Technology"))
        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.TitleItem("Healthcare"))
        itemList.add(InstrumentItem.BlankItem())
        takeInstrumentWithSector(instruments, "healthcare").forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(InstrumentItem.InstrumentViewMoreItem("Healthcare"))
        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.TitleItem("Financial Service"))
        itemList.add(InstrumentItem.BlankItem())
        takeInstrumentWithSector(
            instruments,
            "financial-services"
        ).forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(InstrumentItem.InstrumentViewMoreItem("Financial Service"))
        return itemList
    }

    private fun addInstrument(
        itemList: ArrayList<InstrumentItem>,
        index: Int,
        instrument: Instrument
    ) {
        if (index > 0) {
            itemList.add(InstrumentItem.DividerItem())
        }
        itemList.add(InstrumentItem.InstrumentListItem(instrument))
    }

    private fun takeInstrumentWithCategory(
        instruments: List<Instrument>,
        category: String
    ): List<Instrument> =
        instruments.filter { it.categories?.contains(category) == true }.take(RECOMMENDED_LIMIT)

    private fun takeInstrumentWithSector(
        instruments: List<Instrument>,
        sector: String
    ): List<Instrument> =
        instruments.filter { it.sector?.contains(sector) == true }.take(RECOMMENDED_LIMIT)
}