package com.awonar.app.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.user.StatGainResponse
import com.awonar.android.shared.domain.profile.GetDrawdownUseCase
import com.awonar.android.shared.domain.profile.GetGrowthStatisticUseCase
import com.awonar.android.shared.domain.profile.GetRiskStatisticUseCase
import com.awonar.app.domain.profile.ConvertGrowthRequest
import com.awonar.app.domain.profile.ConvertGrowthStatisticToItemUseCase
import com.awonar.app.domain.profile.ConvertRiskStatisticToItemUseCase
import com.awonar.app.domain.profile.StatRiskItemRequest
import com.awonar.app.ui.profile.stat.StatisticItem
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getGrowthStatisticUseCase: GetGrowthStatisticUseCase,
    private val getRiskStatisticUseCase: GetRiskStatisticUseCase,
    private val getDrawdownUseCase: GetDrawdownUseCase,
    private val convertGrowthStatisticToItemUseCase: ConvertGrowthStatisticToItemUseCase,
    private val convertRiskStatisticToItemUseCase: ConvertRiskStatisticToItemUseCase,
) : ViewModel() {
    private val _currentYear = MutableStateFlow(Calendar.getInstance().get(Calendar.YEAR))
    private val _isShowMore = MutableStateFlow(false)
    private val _growthInfo = MutableStateFlow<StatGainResponse?>(null)

    private val _gainItemsState = MutableStateFlow<MutableList<StatisticItem>>(mutableListOf())
    val gainItemsState: StateFlow<MutableList<StatisticItem>> get() = _gainItemsState

    private val _riskItemsState = MutableStateFlow<MutableList<StatisticItem>>(mutableListOf())
    val riskItemsState: StateFlow<MutableList<StatisticItem>> get() = _riskItemsState

    fun changeGrowthYear(year: Int) {
        _currentYear.value = year
        viewModelScope.launch {
            if (_growthInfo.value != null) {
                _gainItemsState.value =
                    convertGrowthStatisticToItemUseCase(ConvertGrowthRequest(
                        stat = _growthInfo.value!!,
                        year = _currentYear.value,
                        isShowMore = _isShowMore.value
                    )).successOr(mutableListOf())
            }
        }
    }

    fun getGrowthStatistic(uid: String) {
        viewModelScope.launch {
            getGrowthStatisticUseCase(uid).collect {
                _growthInfo.value = it.successOr(null)
                if (_growthInfo.value != null) {
                    _gainItemsState.value =
                        convertGrowthStatisticToItemUseCase(ConvertGrowthRequest(
                            stat = _growthInfo.value!!,
                            year = _currentYear.value,
                            isShowMore = _isShowMore.value
                        )).successOr(
                            mutableListOf())
                }
            }
        }
    }

    fun getRiskStatistic(uid: String) {
        viewModelScope.launch {
            val result = combine(getRiskStatisticUseCase(uid),
                getDrawdownUseCase(uid)) { statRisk, statDrawdown ->
                convertRiskStatisticToItemUseCase(StatRiskItemRequest(
                    statRisk.successOr(null),
                    statDrawdown.successOr(null)
                )).successOr(mutableListOf())
            }
            result.collect {
                _riskItemsState.value = it
            }
        }
    }

    fun toggleShowMoreGrowth() {
        viewModelScope.launch {
            _isShowMore.value = !_isShowMore.value
            if (_growthInfo.value != null) {
                _gainItemsState.value =
                    convertGrowthStatisticToItemUseCase(ConvertGrowthRequest(
                        stat = _growthInfo.value!!,
                        year = _currentYear.value,
                        isShowMore = _isShowMore.value
                    )).successOr(mutableListOf())
            }
        }
    }
}
