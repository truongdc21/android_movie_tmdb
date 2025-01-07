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
package com.truongdc.movie.feature.login

import androidx.lifecycle.viewModelScope
import com.truongdc.movie.core.data.repository.MainRepository
import com.truongdc.movie.core.state.UiStateDelegateImpl
import com.truongdc.movie.core.viewmodel.UiStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : UiStateViewModel<LoginViewModel.UiState, LoginViewModel.Event>(
    UiStateDelegateImpl(UiState()),
) {
    data class UiState(
        val email: String = "",
        val pass: String = "",
        val isTextFieldFocused: Boolean = false,
        val isInValid: Boolean = true,
    )

    sealed interface Event {
        data object LoginSuccess : Event

        data object LoginFailed : Event
    }

    fun onEmailChange(email: String) = viewModelScope.launch {
        updateUiState { state -> state.copy(email = email) }
    }

    fun onPassChange(pass: String) = viewModelScope.launch {
        updateUiState { state -> state.copy(pass = pass) }
        checkInvalid()
    }

    fun onUpdateTextFiledFocus(isFocus: Boolean) = viewModelScope.launch {
        updateUiState { state -> state.copy(isTextFieldFocused = isFocus) }
        checkInvalid()
    }

    private fun checkInvalid() {
        val isInValid = uiState.email.length < 6 || uiState.pass.length < 6
        asyncUpdateUiState(viewModelScope) { state -> state.copy(isInValid = isInValid) }
    }

    fun onSubmitLogin(email: String, pass: String) = viewModelScope.launch {
        mainRepository.user.firstOrNull()?.let { user ->
            showLoading()
            if (user.email == email && user.password == pass) {
                mainRepository.setIsLogin(true)
                sendEvent(Event.LoginSuccess)
                hideLoading()
            } else {
                sendEvent(Event.LoginFailed)
                hideLoading()
            }
        }
    }
}
