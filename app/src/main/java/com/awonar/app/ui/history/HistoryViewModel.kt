package com.awonar.app.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.awonar.android.model.history.Aggregate
import com.awonar.android.model.history.History
import com.awonar.android.model.history.HistoryResponse
import com.awonar.android.shared.domain.history.GetAggregateUseCase
import com.awonar.android.shared.domain.history.GetHistoryUseCase
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getAggregateUseCase: GetAggregateUseCase,
    private val getHistoryUseCase: GetHistoryUseCase
) : ViewModel() {

    val aggregateState: StateFlow<Aggregate?> = flow {
        val prev7Day = Calendar.getInstance()
        prev7Day.add(Calendar.DATE, -7)
        getAggregateUseCase(prev7Day.timeInMillis / 1000).collect {
            emit(it.successOr(null))
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    val historiesState: StateFlow<PagingData<History>> = flow {
        getHistoryUseCase(1).collect {
            emit(it.successOr(PagingData.empty()))
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, PagingData.empty())
}