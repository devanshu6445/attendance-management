package com.college.attendance.management

import androidx.compose.runtime.currentComposer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

abstract class BaseViewModel<S> : ViewModel() {

    abstract val initialState: S

    protected abstract val uiFlow: Flow<S>

    @Inject
    lateinit var userDetailsDataStore: UserDetailsDataStore

    val uiState: StateFlow<S> by lazy {
        uiFlow
            .flowOn(Dispatchers.Main)
            .stateIn(viewModelScope, started = WhileSubscribed(), initialValue = initialState)
    }

    fun MutableStateFlow<S>.updateState(block: S.() -> S) {
        update { it.block() }
    }

    fun logout() {
        userDetailsDataStore.value = flowOf()
    }
}