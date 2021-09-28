package com.awonar.app.ui.launcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.shared.db.room.TradingData
import com.awonar.android.shared.domain.order.GetTradingDataUseCase
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(
    private val getTradingDataUseCase: GetTradingDataUseCase
) : ViewModel() {

    val loadTradingData: StateFlow<List<TradingData>?> = flow {
        getTradingDataUseCase(Unit).collect {
            val isAuth = it.successOr(emptyList())
            emit(isAuth)
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

}