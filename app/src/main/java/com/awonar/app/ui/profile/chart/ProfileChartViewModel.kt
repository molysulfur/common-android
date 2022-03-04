package com.awonar.app.ui.profile.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.profile.PublicAllocate
import com.awonar.android.model.profile.PublicAllocateRequest
import com.awonar.android.model.profile.PublicExposure
import com.awonar.android.model.profile.PublicExposureRequest
import com.awonar.android.model.user.User
import com.awonar.android.shared.domain.profile.GetPublicAllocateUseCase
import com.awonar.android.shared.domain.profile.GetPublicExposureUseCase
import com.awonar.app.domain.profile.*
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartItem
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileChartViewModel @Inject constructor(
    private val getPublicExposureUseCase: GetPublicExposureUseCase,
    private val getPublicAllocateUseCase: GetPublicAllocateUseCase,
    private val convertPublicExposureUseCase: ConvertPublicExposureUseCase,
    private val convertPublicAllocateUseCase: ConvertPublicAllocateUseCase,
    private val convertPublicExposureLevel2UseCase: ConvertPublicExposureLevel2UseCase,
    private val convertPublicAllocateLevel2UseCase: ConvertPublicAllocateLevel2UseCase,
) : ViewModel() {
    private val _type = MutableStateFlow("exposure")

    private val _positionChartItems: MutableStateFlow<MutableList<PositionChartItem>> =
        MutableStateFlow(mutableListOf(PositionChartItem.LoadingItem()))
    val positionChartItems: StateFlow<MutableList<PositionChartItem>> get() = _positionChartItems

    fun changeType(type: String?) {
        _type.value = type ?: ""
    }

    fun getInsideChart(user: User?, category: String?) {
        when (_type.value) {
            "allocate" -> getAllocate(user, category)
            "exposure" -> getExposure(user, category)
        }
    }

    private fun getAllocate(user: User?, category: String?) {
        viewModelScope.launch {
            when (category) {
                "markets" -> getPublicAllocateUseCase(
                    PublicAllocateRequest(username = user?.username,
                        category)).collect {
                    val data = it.successOr(emptyList()) ?: emptyList()
                    val itemList =
                        convertPublicAllocateLevel2UseCase(data).successOr(mutableListOf())
                    _positionChartItems.value = itemList
                }
                else -> getAllocate(value = user)
            }
        }
    }

    private fun getExposure(user: User?, category: String?) {
        viewModelScope.launch {
            when (category) {
                "stocks", "currencies", "crypto", "commodity", "etf" -> getPublicExposureUseCase(
                    PublicExposureRequest(username = user?.username,
                        category)).collect {
                    val data = it.successOr(emptyList()) ?: emptyList()
                    val itemList =
                        convertPublicExposureLevel2UseCase(data).successOr(mutableListOf())
                    _positionChartItems.value = itemList
                }
                else -> getExposure(value = user)
            }
        }
    }

    fun getExposure(value: User?) {
        viewModelScope.launch {
            getPublicExposureUseCase(PublicExposureRequest(username = value?.username,
                null)).collect {
                val data = it.successOr(emptyList()) ?: emptyList()
                val itemList =
                    convertPublicExposureUseCase(data).successOr(mutableListOf())
                _positionChartItems.value = itemList
            }
        }
    }

    fun getAllocate(value: User?) {
        viewModelScope.launch {
            getPublicAllocateUseCase(PublicAllocateRequest(username = value?.username,
                null)).collect {
                val data = it.successOr(emptyList()) ?: emptyList()
                val itemList =
                    convertPublicAllocateUseCase(data).successOr(mutableListOf())
                _positionChartItems.value = itemList
            }
        }
    }


}