package com.awonar.app.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingViewModel : ViewModel() {

    private val _names = MutableStateFlow<String?>(null)
    val names: StateFlow<String?> = _names
    val addedName = MutableStateFlow<String?>(null)

    /**
     * Loading names from the repo async on the background thread and updating the StateFlow
     * ether with data received in the case of success or an error message.
     */
    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                TestRepo().loadFriendsNames()
                    .combine(addedName) { listFromDataSource, addedName ->
                        if (addedName.isNullOrEmpty())
                            listFromDataSource
                        else "$listFromDataSource, $addedName"
                    }
                    .flowOn(Dispatchers.IO)
                    .collect {
                        println("I'm working in thread ${Thread.currentThread().name}")
                        _names.value = it
                    }
            } catch (ex: Exception) {
                _names.value = "Something went wrong"
            }
        }

    }

}

class TestRepo {

    fun loadFriendsNames(): Flow<String> = flow {
        emit(listOf("Ana", "Mary", "James", "Edward").joinToString())
    }
}