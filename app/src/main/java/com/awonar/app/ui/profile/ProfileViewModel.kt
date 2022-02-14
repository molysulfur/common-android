package com.awonar.app.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.user.StatGainDayRequest
import com.awonar.android.model.user.StatGainResponse
import com.awonar.android.shared.domain.profile.GetDrawdownUseCase
import com.awonar.android.shared.domain.profile.GetGrowthDayStatisticUseCase
import com.awonar.android.shared.domain.profile.GetGrowthStatisticUseCase
import com.awonar.android.shared.domain.profile.GetRiskStatisticUseCase
import com.awonar.app.domain.profile.ConvertGrowthRequest
import com.awonar.app.domain.profile.ConvertGrowthStatisticToItemUseCase
import com.awonar.app.domain.profile.ConvertRiskStatisticToItemUseCase
import com.awonar.app.domain.profile.StatRiskItemRequest
import com.awonar.app.ui.profile.stat.StatisticItem
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.data
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getGrowthStatisticUseCase: GetGrowthStatisticUseCase,
    private val getGrowthDayStatisticUseCase: GetGrowthDayStatisticUseCase,
    private val getRiskStatisticUseCase: GetRiskStatisticUseCase,
    private val getDrawdownUseCase: GetDrawdownUseCase,
    private val convertGrowthStatisticToItemUseCase: ConvertGrowthStatisticToItemUseCase,
    private val convertRiskStatisticToItemUseCase: ConvertRiskStatisticToItemUseCase,
) : ViewModel() {
    private val _currentYear = MutableStateFlow(Calendar.getInstance().get(Calendar.YEAR))
    private val _isShowMore = MutableStateFlow(false)
    private val _isShowGrowthChart = MutableStateFlow(false)
    private val _growthInfo = MutableStateFlow<StatGainResponse?>(null)
    private val _dayGrowthInfo = MutableStateFlow<Map<String, Float>>(emptyMap())
    private val _userId = MutableStateFlow<String?>(null)

    private val _gainItemsState = MutableStateFlow<MutableList<StatisticItem>>(mutableListOf())
    val gainItemsState: StateFlow<MutableList<StatisticItem>> get() = _gainItemsState

    private val _riskItemsState = MutableStateFlow<MutableList<StatisticItem>>(mutableListOf())
    val riskItemsState: StateFlow<MutableList<StatisticItem>> get() = _riskItemsState

    fun getGrowthStatistic(uid: String, year: Int) {
        viewModelScope.launch {
            if (!uid.isNullOrBlank()) {
                _userId.value = uid
            }
            _currentYear.value = year
            val dayGrowth =
                getGrowthDayStatisticUseCase(StatGainDayRequest(_userId.value ?: "",
                    "${_currentYear.value}"))
            val monthGrowth = getGrowthStatisticUseCase(_userId.value ?: "")
            val growthResult = combine(dayGrowth, monthGrowth) { dayGrowthInfo, growthInfo ->
                var itemList = mutableListOf<StatisticItem>()
                if (dayGrowthInfo is Result.Success && growthInfo is Result.Success) {
                    _dayGrowthInfo.value = dayGrowthInfo.successOr(emptyMap()) ?: emptyMap()
                    _growthInfo.value = growthInfo.successOr(null)
                    if (_growthInfo.value != null) {
                        itemList = convertGrowthStatisticToItemUseCase(ConvertGrowthRequest(
                            stat = _growthInfo.value!!,
                            year = _currentYear.value,
                            statDay = _dayGrowthInfo.value,
                            isShowGrowth = _isShowGrowthChart.value,
                            isShowMore = _isShowMore.value
                        )).successOr(
                            mutableListOf())
                    }
                }
                itemList
            }
            growthResult.collect { itemList ->
                _gainItemsState.value = itemList
            }
        }
    }

    fun getRiskStatistic(uid: String) {
        viewModelScope.launch {
            val result = combine(
                getRiskStatisticUseCase(uid),
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
                        statDay = _dayGrowthInfo.value,
                        isShowGrowth = _isShowGrowthChart.value,
                        isShowMore = _isShowMore.value
                    )).successOr(mutableListOf())
            }
        }
    }

    fun changeTypeGrowth(isShow: Boolean) {
        _isShowGrowthChart.value = isShow
        getGrowthStatistic(_userId.value ?: "", _currentYear.value)
    }
}
