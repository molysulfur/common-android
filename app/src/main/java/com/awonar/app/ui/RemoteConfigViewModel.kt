package com.awonar.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.settting.Bank
import com.awonar.android.model.settting.Country
import com.awonar.android.shared.domain.remote.GetBanksUseCase
import com.awonar.android.shared.domain.remote.GetCountriesUseCase
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.molysulfur.library.result.data
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RemoteConfigViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val getBanksUseCase: GetBanksUseCase
) : ViewModel() {

    val countryState: StateFlow<List<Country>?> = getCountriesUseCase(Unit).map { result ->
        result.data
    }.stateIn(viewModelScope, WhileViewSubscribed, listOf())

    val bankState: StateFlow<List<Bank>?> = getBanksUseCase(Unit).map { result ->
        result.data
    }.stateIn(viewModelScope, WhileViewSubscribed, listOf())

    val accountTypeState: StateFlow<List<String>?> = flow {
        emit(listOf("Savings Account", "Fixed Deposit Account", "Current Account"))
    }.stateIn(viewModelScope, WhileViewSubscribed, listOf())
}