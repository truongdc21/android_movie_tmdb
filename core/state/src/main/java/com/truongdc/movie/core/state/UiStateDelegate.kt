/*
 * Designed and developed by 2024 truongdc21 (Dang Chi Truong)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.truongdc.movie.core.state

import com.truongdc.movie.core.common.exception.base.AppExceptionWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface UiStateDelegate<UiState, Event> {

    val uiStateFlow: StateFlow<UiState>

    val singleEvents: Flow<Event>

    val isLoading: StateFlow<Boolean>

    val appExceptionWrapper: Flow<AppExceptionWrapper>

    fun showLoading()

    fun hideLoading()

    suspend fun sendAppException(appExceptionWrapper: AppExceptionWrapper)

    val UiStateDelegate<UiState, Event>.uiState: UiState

    suspend fun UiStateDelegate<UiState, Event>.updateUiState(
        transform: (uiState: UiState) -> UiState,
    )

    fun UiStateDelegate<UiState, Event>.asyncUpdateUiState(
        coroutineScope: CoroutineScope,
        transform: (uiState: UiState) -> UiState,
    ): Job

    suspend fun UiStateDelegate<UiState, Event>.sendEvent(event: Event)
}

/**
 * @param mutexState A mutex for synchronizing state access.
 * @param mutexError A mutex for synchronizing error access.
 * @param initialUiState Initial UI state.
 * @param singleLiveEventCapacity Channel capacity for SingleLiveEvent.
 */
class UiStateDelegateImpl<UiState, Event>(
    initialUiState: UiState,
    singleLiveEventCapacity: Int = Channel.BUFFERED,
    private val mutexState: Mutex = Mutex(),
    private val mutexError: Mutex = Mutex(),
) : UiStateDelegate<UiState, Event> {

    private val uiMutableStateFlow = MutableStateFlow(initialUiState)
    private val singleEventChannel = Channel<Event>(singleLiveEventCapacity)
    private val isLoadingStateFlow = MutableStateFlow(false)
    private val appExceptionWrapperChanel = Channel<AppExceptionWrapper>(singleLiveEventCapacity)

    override val uiStateFlow: StateFlow<UiState>
        get() = uiMutableStateFlow.asStateFlow()

    override val singleEvents: Flow<Event>
        get() = singleEventChannel.receiveAsFlow()

    override val isLoading: StateFlow<Boolean>
        get() = isLoadingStateFlow

    override val appExceptionWrapper: Flow<AppExceptionWrapper>
        get() = appExceptionWrapperChanel.receiveAsFlow()

    override fun showLoading() {
        isLoadingStateFlow.value = true
    }

    override fun hideLoading() {
        isLoadingStateFlow.value = false
    }

    override suspend fun sendAppException(
        appExceptionWrapper: AppExceptionWrapper,
    ): Unit = mutexError.withLock {
        appExceptionWrapperChanel.send(appExceptionWrapper)
        appExceptionWrapper.exceptionCompleter?.await()
    }

    override val UiStateDelegate<UiState, Event>.uiState: UiState
        get() = uiMutableStateFlow.value

    override suspend fun UiStateDelegate<UiState, Event>.updateUiState(
        transform: (uiState: UiState) -> UiState,
    ) = mutexState.withLock {
        uiMutableStateFlow.emit(transform(uiState))
    }

    override fun UiStateDelegate<UiState, Event>.asyncUpdateUiState(
        coroutineScope: CoroutineScope,
        transform: (uiState: UiState) -> UiState,
    ) = coroutineScope.launch {
        updateUiState { state -> transform(state) }
    }

    override suspend fun UiStateDelegate<UiState, Event>.sendEvent(event: Event) =
        singleEventChannel.send(event)
}
