package com.awonar.app.domain

import com.awonar.android.model.market.Instrument
import com.awonar.android.shared.di.MainDispatcher
import com.awonar.app.ui.market.adapter.InstrumentItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertInstrumentCategoryToItemUseCase @Inject constructor(
    @MainDispatcher dispatcher: CoroutineDispatcher
) : UseCase<List<Instrument>, List<InstrumentItem>>(dispatcher) {

    companion object {
        private val INSTRUMENT_LIMIT = 5
    }

    override suspend fun execute(parameters: List<Instrument>): List<InstrumentItem> {
        val itemList = arrayListOf<InstrumentItem>()
        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.TitleItem("Financial Service"))
        itemList.add(InstrumentItem.BlankItem())
        takeInstrumentWithCategory(
            instruments = parameters,
            category = "financial-services"
        ).forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.InstrumentViewMoreItem("Financial Service"))

        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.TitleItem("Healthcare"))
        itemList.add(InstrumentItem.BlankItem())
        takeInstrumentWithCategory(
            instruments = parameters,
            category = "healthcare"
        ).forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.InstrumentViewMoreItem("Healthcare"))

        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.TitleItem("Technology"))
        itemList.add(InstrumentItem.BlankItem())
        takeInstrumentWithCategory(
            instruments = parameters,
            category = "technology"
        ).forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.InstrumentViewMoreItem("Technology"))

        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.TitleItem("Industrials"))
        itemList.add(InstrumentItem.BlankItem())
        takeInstrumentWithCategory(
            instruments = parameters,
            category = "industrials"
        ).forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.InstrumentViewMoreItem("Industrials"))

        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.TitleItem("Consumer Cyclical"))
        itemList.add(InstrumentItem.BlankItem())
        takeInstrumentWithCategory(
            instruments = parameters,
            category = "consumer-cyclical"
        ).forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.InstrumentViewMoreItem("Consumer Cyclical"))

        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.TitleItem("Real Estate"))
        itemList.add(InstrumentItem.BlankItem())
        takeInstrumentWithCategory(
            instruments = parameters,
            category = "real-estate"
        ).forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.InstrumentViewMoreItem("Real Estate"))

        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.TitleItem("Energy"))
        itemList.add(InstrumentItem.BlankItem())
        takeInstrumentWithCategory(
            instruments = parameters,
            category = "energy"
        ).forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.InstrumentViewMoreItem("Energy"))

        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.TitleItem("Basic Materials"))
        itemList.add(InstrumentItem.BlankItem())
        takeInstrumentWithCategory(
            instruments = parameters,
            category = "basic-materials"
        ).forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.InstrumentViewMoreItem("Basic Materials"))

        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.TitleItem("Consumer Defensive"))
        itemList.add(InstrumentItem.BlankItem())
        takeInstrumentWithCategory(
            instruments = parameters,
            category = "consumer-defensive"
        ).forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.InstrumentViewMoreItem("Consumer Defensive"))

        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.TitleItem("Unility"))
        itemList.add(InstrumentItem.BlankItem())
        takeInstrumentWithCategory(
            instruments = parameters,
            category = "utilities"
        ).forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.InstrumentViewMoreItem("Unility"))

        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.TitleItem("Communication Services"))
        itemList.add(InstrumentItem.BlankItem())
        takeInstrumentWithCategory(
            instruments = parameters,
            category = "communication-services"
        ).forEachIndexed { index, instrument ->
            addInstrument(itemList, index, instrument)
        }
        itemList.add(InstrumentItem.BlankItem())
        itemList.add(InstrumentItem.InstrumentViewMoreItem("Communication Services"))
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
        instruments.filter { it.sector?.contains(category) == true }.take(INSTRUMENT_LIMIT)

}