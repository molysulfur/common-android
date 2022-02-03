package com.awonar.app.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.shared.domain.profile.GetGrowthStatisticUseCase
import com.awonar.app.domain.portfolio.ConvertGrowthStatisticToItemUseCase
import com.awonar.app.ui.profile.stat.StatisticItem
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getGrowthStatisticUseCase: GetGrowthStatisticUseCase,
    private val convertGrowthStatisticToItemUseCase: ConvertGrowthStatisticToItemUseCase,
) : ViewModel() {

    private val _statisticState = MutableStateFlow<MutableList<StatisticItem>>(mutableListOf())
    val statisticState: StateFlow<MutableList<StatisticItem>> get() = _statisticState

    fun getGrowthStatistic(uid: String) {
        viewModelScope.launch {
            getGrowthStatisticUseCase(uid).collect {
                val growthInfo = it.successOr(null)
                if (growthInfo != null) {
                    _statisticState.value = convertGrowthStatisticToItemUseCase(growthInfo).successOr(mutableListOf())
                }
            }
        }
    }
}