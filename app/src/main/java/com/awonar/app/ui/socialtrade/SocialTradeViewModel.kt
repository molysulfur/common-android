package com.awonar.app.ui.socialtrade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.socialtrade.CopierRecommended
import com.awonar.android.shared.domain.socialtrade.GetRecommendedUseCase
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SocialTradeViewModel @Inject constructor(
    private val getRecommendedUseCase: GetRecommendedUseCase
) : ViewModel() {

    val recommendedState: StateFlow<List<CopierRecommended>?> = flow {
        getRecommendedUseCase(Unit).collect {
            emit(it.successOr(emptyList()))
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, listOf())
}