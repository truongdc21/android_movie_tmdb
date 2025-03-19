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
package com.truongdc.movie.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truongdc.movie.core.common.exception.base.AppException
import com.truongdc.movie.core.common.exception.base.AppExceptionWrapper
import com.truongdc.movie.core.navigation.AppNavigator
import com.truongdc.movie.core.state.UiStateDelegate
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

abstract class BaseViewModel<UiState, Event> : ViewModel() {

    @Inject
    lateinit var appNavigator: AppNavigator

    fun UiStateDelegate<UiState, Event>.launchSafeTask(
        doOnRetry: (suspend () -> Unit)? = null,
        doOnError: (suspend (AppException) -> Unit)? = null,
        doOnSubscribe: (suspend () -> Unit)? = null,
        doOnSuccessOrError: (suspend () -> Unit)? = null,
        doOnEventCompleted: (suspend () -> Unit)? = null,
        handleLoading: Boolean = true,
        handleError: Boolean = true,
        handleRetry: Boolean = true,
        onForceHandleError: ((AppException) -> Boolean)? = null,
        overrideErrorMessage: String? = null,
        maxRetries: Int? = null,
        action: suspend CoroutineScope.() -> Unit,
    ) = viewModelScope.launch {
        runSafeTask(
            action = action,
            doOnRetry = doOnRetry,
            doOnError = doOnError,
            doOnSubscribe = doOnSubscribe,
            doOnSuccessOrError = doOnSuccessOrError,
            doOnEventCompleted = doOnEventCompleted,
            handleLoading = handleLoading,
            handleError = handleError,
            handleRetry = handleRetry,
            onForceHandleError = onForceHandleError,
            overrideErrorMessage = overrideErrorMessage,
            maxRetries = maxRetries,
            coroutineScope = viewModelScope,
        )
    }

    private suspend fun UiStateDelegate<UiState, Event>.runSafeTask(
        action: suspend CoroutineScope.() -> Unit,
        doOnRetry: (suspend () -> Unit)? = null,
        doOnError: (suspend (AppException) -> Unit)? = null,
        doOnSubscribe: (suspend () -> Unit)? = null,
        doOnSuccessOrError: (suspend () -> Unit)? = null,
        doOnEventCompleted: (suspend () -> Unit)? = null,
        handleLoading: Boolean = true,
        handleError: Boolean = true,
        handleRetry: Boolean = true,
        onForceHandleError: ((AppException) -> Boolean)? = null,
        overrideErrorMessage: String? = null,
        maxRetries: Int? = null,
        coroutineScope: CoroutineScope,
    ) {
        require(maxRetries == null || maxRetries > 0) { "maxRetries must be positive" }
        var recursion: CompletableDeferred<Unit>? = null
        try {
            doOnSubscribe?.invoke()
            if (handleLoading) {
                showLoading()
            }

            action(coroutineScope)

            if (handleLoading) {
                hideLoading()
            }
            doOnSuccessOrError?.invoke()
        } catch (e: AppException) {
            if (handleLoading) {
                hideLoading()
            }
            doOnSuccessOrError?.invoke()
            doOnError?.invoke(e)

            if (handleError || (onForceHandleError?.invoke(e) ?: shouldForceHandleError(e))) {
                sendAppException(
                    AppExceptionWrapper(
                        appException = e,
                        doOnRetry = doOnRetry ?: if (handleRetry && maxRetries != 1) {
                            {
                                withContext(NonCancellable) {
                                    recursion = CompletableDeferred()
                                    runSafeTask(
                                        action = action,
                                        doOnEventCompleted = doOnEventCompleted,
                                        doOnSubscribe = doOnSubscribe,
                                        doOnSuccessOrError = doOnSuccessOrError,
                                        doOnError = doOnError,
                                        doOnRetry = doOnRetry,
                                        onForceHandleError = onForceHandleError,
                                        handleError = handleError,
                                        handleLoading = handleLoading,
                                        handleRetry = handleRetry,
                                        overrideErrorMessage = overrideErrorMessage,
                                        maxRetries = maxRetries?.minus(1),
                                        coroutineScope = coroutineScope,
                                    )
                                    recursion?.complete(Unit)
                                }
                            }
                        } else {
                            null
                        },
                        exceptionCompleter = CompletableDeferred(),
                        overrideMessage = overrideErrorMessage,
                    ),
                )
            }
        } finally {
            recursion?.await()
            doOnEventCompleted?.invoke()
        }
    }

    private fun shouldForceHandleError(appException: AppException): Boolean {
        // return appException is RemoteException && appException.kind == RemoteExceptionKind.refreshTokenFailed;
        return false
    }
}
