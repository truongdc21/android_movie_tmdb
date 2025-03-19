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
package com.truongdc.movie.core.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.truongdc.movie.core.common.constant.Constants
import com.truongdc.movie.core.common.exception.base.AppExceptionType
import com.truongdc.movie.core.common.exception.base.AppExceptionWrapper
import com.truongdc.movie.core.designsystem.components.LoadingContent
import com.truongdc.movie.core.state.UiStateDelegate
import com.truongdc.movie.core.state.extensions.CollectAppExceptionWrapperEffect
import com.truongdc.movie.core.state.extensions.CollectEventEffect
import com.truongdc.movie.core.state.extensions.collectLoadingWithLifecycle
import com.truongdc.movie.core.state.extensions.collectWithLifecycle
import com.truongdc.movie.core.ui.exception.ExceptionMessageMapper
import com.truongdc.movie.core.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import com.truongdc.movie.core.designsystem.R.string as string

@Composable
fun <UiState, Event> UiStateContent(
    uiStateDelegate: UiStateDelegate<UiState, Event>,
    modifier: Modifier = Modifier,
    onEventEffect: (event: Event) -> Unit = {},
    onDismissErrorDialog: () -> Unit = {},
    content: @Composable (uiState: UiState) -> Unit,
) {
    val viewModel = uiStateDelegate as BaseViewModel<*, *>

    val uiState by uiStateDelegate.collectWithLifecycle()

    val isLoading by uiStateDelegate.collectLoadingWithLifecycle()

    val exceptionMessageMapper = ExceptionMessageMapper()

    val (errorDialogVisible, setErrorDialogVisible) = remember { mutableStateOf(false) }

    val (errorDialogRetryVisible, setErrorDialogRetryVisible) = remember { mutableStateOf(false) }

    val currentMessageError = remember { mutableStateOf<String?>(null) }

    val currentWrapperError = remember { mutableStateOf<AppExceptionWrapper?>(null) }

    uiStateDelegate.CollectEventEffect { event -> onEventEffect(event) }

    uiStateDelegate.CollectAppExceptionWrapperEffect { wrapper ->
        handleExceptionWrapper(
            wrapper,
            exceptionMessageMapper.map(wrapper.appException),
            onShowDialog = { message ->
                currentMessageError.value = message
                currentWrapperError.value = wrapper
                setErrorDialogVisible(true)
            },
            onShowDialogRetry = { message ->
                currentMessageError.value = message
                currentWrapperError.value = wrapper
                setErrorDialogRetryVisible(true)
            },
            onShowSnackBar = { message ->
                viewModel.appNavigator.displaySnackBar(
                    message,
                    duration = Constants.DURATION_SNACK_BAR_SECONDS,
                )
                wrapper.exceptionCompleter?.complete(Unit)
            },
        )
    }

    LoadingContent(
        isLoading = isLoading,
        modifier = modifier,
    ) {
        content(uiState)
    }

    if (errorDialogVisible && currentMessageError.value != null && currentWrapperError.value != null) {
        ErrorDialog(
            message = currentMessageError.value ?: stringResource(string.unexpected_error),
            onDismiss = {
                currentWrapperError.value?.exceptionCompleter?.complete(Unit)
                setErrorDialogVisible(false)
                onDismissErrorDialog()
            },
        )
    }

    if (errorDialogRetryVisible && currentMessageError.value != null && currentWrapperError.value != null) {
        ErrorDialogRetry(
            message = currentMessageError.value ?: stringResource(string.unexpected_error),
            onDismiss = { isRetry ->
                currentWrapperError.value?.exceptionCompleter?.complete(Unit)
                if (!isRetry) {
                    setErrorDialogRetryVisible(false)
                    onDismissErrorDialog()
                } else {
                    setErrorDialogRetryVisible(false)
                }
            },
            appExceptionWrapper = currentWrapperError.value!!,
        )
    }
}

@Composable
private fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(string.error)) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(string.ok))
            }
        },
    )
}

@Composable
private fun ErrorDialogRetry(
    message: String,
    // / true: retry, false: cancel
    onDismiss: (Boolean) -> Unit,
    appExceptionWrapper: AppExceptionWrapper,
) {
    val coroutineScope = rememberCoroutineScope()
    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        title = { Text(text = stringResource(string.error)) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = {
                if (appExceptionWrapper.doOnRetry == null) {
                    onDismiss(false)
                } else {
                    onDismiss(true)
                    coroutineScope.launch {
                        appExceptionWrapper.doOnRetry?.invoke()
                    }
                }
            }) {
                Text(stringResource(string.retry))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss(false) }) {
                Text(stringResource(string.cancel))
            }
        },
    )
}

private fun handleExceptionWrapper(
    appExceptionWrapper: AppExceptionWrapper,
    commonExceptionMessage: String,
    onShowDialog: (String) -> Unit,
    onShowDialogRetry: (String) -> Unit,
    onShowSnackBar: (String) -> Unit,
) {
    val message = appExceptionWrapper.overrideMessage ?: commonExceptionMessage
    when (appExceptionWrapper.appException.appExceptionType) {
        AppExceptionType.REMOTE -> onShowDialogRetry(message)
        AppExceptionType.PARSE -> onShowSnackBar(message)
        AppExceptionType.REMOTE_CONFIG -> onShowDialogRetry(message)
        AppExceptionType.UNCAUGHT -> onShowDialog(message)
        AppExceptionType.VALIDATION -> onShowSnackBar(message)
        AppExceptionType.FIRESTORE -> onShowDialog(message)
        AppExceptionType.AUTH -> onShowDialog(message)
    }
}
