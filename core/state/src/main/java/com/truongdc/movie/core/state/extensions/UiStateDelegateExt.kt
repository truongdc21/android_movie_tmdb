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
package com.truongdc.movie.core.state.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.truongdc.movie.core.common.exception.base.AppExceptionWrapper
import com.truongdc.movie.core.state.UiStateDelegate
import kotlinx.coroutines.flow.FlowCollector

@Composable
fun <R> UiStateDelegate<R, *>.collectUiState() = this.uiStateFlow.collectAsState()

@Composable
fun <R> UiStateDelegate<R, *>.collectWithLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
) = this.uiStateFlow.collectAsStateWithLifecycle(
    minActiveState = minActiveState,
)

@Composable
fun <R> UiStateDelegate<R, *>.collectLoadingWithLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
) = this.isLoading.collectAsStateWithLifecycle(
    minActiveState = minActiveState,
)

@Composable
fun <State, Event> UiStateDelegate<State, Event>.CollectEventEffect(
    lifecycleOwner: LifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current,
    lifecycleState: Lifecycle.State = Lifecycle.State.RESUMED,
    vararg keys: Any?,
    collector: FlowCollector<Event>,
) = LaunchedEffect(Unit, *keys) {
    singleEvents.flowWithLifecycle(
        lifecycle = lifecycleOwner.lifecycle,
        minActiveState = lifecycleState,
    ).collect(collector)
}

@Composable
fun UiStateDelegate<*, *>.CollectAppExceptionWrapperEffect(
    lifecycleOwner: LifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current,
    lifecycleState: Lifecycle.State = Lifecycle.State.RESUMED,
    vararg keys: Any?,
    collector: FlowCollector<AppExceptionWrapper>,
) = LaunchedEffect(Unit, *keys) {
    appExceptionWrapper.flowWithLifecycle(
        lifecycle = lifecycleOwner.lifecycle,
        minActiveState = lifecycleState,
    ).collect(collector)
}
