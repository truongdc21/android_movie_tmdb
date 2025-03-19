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

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.truongdc.movie.core.state.UiStateDelegate
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun <State, Event> UiStateDelegate<State, Event>.collectEvent(
    lifecycle: Lifecycle,
    lifecycleState: Lifecycle.State = Lifecycle.State.RESUMED,
    block: (event: Event) -> Unit,
): Job = lifecycle.coroutineScope.launch {
    singleEvents.flowWithLifecycle(
        lifecycle = lifecycle,
        minActiveState = lifecycleState,
    ).collect {
        block(it)
    }
}

fun <State, Event> UiStateDelegate<State, Event>.collectEvent(
    lifecycleOwner: LifecycleOwner,
    lifecycleState: Lifecycle.State = Lifecycle.State.RESUMED,
    block: (event: Event) -> Unit,
): Job = lifecycleOwner.lifecycleScope.launch {
    singleEvents.flowWithLifecycle(
        lifecycle = lifecycleOwner.lifecycle,
        minActiveState = lifecycleState,
    ).collect { event ->
        block.invoke(event)
    }
}

fun <State, Event> UiStateDelegate<State, Event>.collectLoading(
    lifecycle: Lifecycle,
    lifecycleState: Lifecycle.State = Lifecycle.State.RESUMED,
    block: (Boolean) -> Unit,
): Job = lifecycle.coroutineScope.launch {
    isLoading.flowWithLifecycle(
        lifecycle = lifecycle,
        minActiveState = lifecycleState,
    ).collect {
        block(it)
    }
}
