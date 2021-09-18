package com.awonar.app.ui.marketprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.market.InstrumentProfile
import com.awonar.android.shared.domain.marketprofile.GetMarketProfileUseCase
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketProfileViewModel @Inject constructor(
    private val getMarketProfileUseCase: GetMarketProfileUseCase
) : ViewModel() {

    private val _instrumentState = MutableStateFlow<InstrumentProfile?>(null)
    val instrumentState: StateFlow<InstrumentProfile?> get() = _instrumentState

    fun getInstrumentProfile(id: Int) {
        viewModelScope.launch {
            getMarketProfileUseCase(id).collect {
                _instrumentState.value = it.successOr(null)
            }
        }
    }


}