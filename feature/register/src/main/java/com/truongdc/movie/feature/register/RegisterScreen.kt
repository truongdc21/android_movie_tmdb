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
package com.truongdc.movie.feature.register

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.truongdc.movie.core.designsystem.R.string
import com.truongdc.movie.core.designsystem.components.ObserverKeyBoard
import com.truongdc.movie.core.designsystem.components.PrimaryButton
import com.truongdc.movie.core.designsystem.components.PrimaryTextField
import com.truongdc.movie.core.designsystem.theme.AppTheme
import com.truongdc.movie.core.designsystem.theme.MovieTMDBTheme
import com.truongdc.movie.core.model.User
import com.truongdc.movie.core.ui.UiStateContent
import com.truongdc.movie.core.ui.extensions.showToast

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
) {
    val context = LocalContext.current
    UiStateContent(
        uiStateDelegate = viewModel,
        modifier = Modifier,
        onEventEffect = { event ->
            when (event) {
                RegisterViewModel.Event.RegisterSuccess -> {
                    context.showToast(context.getString(string.register_success))
                    onNavigateBack.invoke()
                }

                RegisterViewModel.Event.RegisterFailed -> {
                    context.showToast(context.getString(string.register_failed_please_try_again))
                }
            }
        },
        content = { uiState ->
            RegisterContent(
                email = uiState.email,
                name = uiState.name,
                pass = uiState.pass,
                isInValid = uiState.isInValid,
                isTextFieldFocused = uiState.isTextFieldFocused,
                onUpdateTextFiledFocus = viewModel::onUpdateTextFiledFocus,
                onEmailChange = viewModel::onEmailChange,
                onNameChange = viewModel::onNameChange,
                onPassChange = viewModel::onPassChange,
                onSubmitRegister = viewModel::onSubmitRegister,
                onNavigateBack = onNavigateBack,
            )
        },
    )
}

@Composable
private fun RegisterContent(
    email: String,
    name: String,
    pass: String,
    isInValid: Boolean,
    isTextFieldFocused: Boolean,
    onUpdateTextFiledFocus: (Boolean) -> Unit,
    onEmailChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onPassChange: (String) -> Unit,
    onSubmitRegister: (User) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold {
        val paddingTopContent = if (AppTheme.orientation.isLandscape()) {
            10.dp
        } else {
            if (!isTextFieldFocused) 150.dp else 0.dp
        }
        val paddingHorizontalContent = if (AppTheme.orientation.isLandscape()) 230.dp else 24.dp
        ObserverKeyBoard(LocalView.current) { isShown ->
            onUpdateTextFiledFocus(isShown)
        }
        Column(
            modifier = Modifier
                .background(AppTheme.colors.primary)
                .fillMaxSize()
                .padding(it)
                .padding(
                    top = paddingTopContent,
                    start = paddingHorizontalContent,
                    end = paddingHorizontalContent,
                ).testTag("register_content"),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = string.create_account),
                style = AppTheme.styles.displaySmall,
                color = AppTheme.colors.onPrimary,
            )
            Text(
                text = stringResource(id = string.register_account_to_login),
                style = AppTheme.styles.bodyLarge,
                color = AppTheme.colors.onPrimary,
            )
            if (AppTheme.orientation.isPortrait()) {
                PrimaryTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    textPlaceholder = stringResource(id = string.mail_id),
                    paddingValues = PaddingValues(
                        top = if (AppTheme.orientation.isLandscape()) 10.dp else 50.dp,
                    ),
                    testTag = "email",
                )
                PrimaryTextField(
                    value = name,
                    onValueChange = onNameChange,
                    textPlaceholder = stringResource(id = string.fullname),
                    paddingValues = PaddingValues(
                        top = if (AppTheme.orientation.isLandscape()) 10.dp else 20.dp,
                    ),
                    testTag = "name",
                )
            } else {
                Row {
                    PrimaryTextField(
                        value = email,
                        onValueChange = onEmailChange,
                        textPlaceholder = stringResource(id = string.mail_id),
                        modifier = Modifier.weight(1f),
                        testTag = "email",
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    PrimaryTextField(
                        value = name,
                        onValueChange = onNameChange,
                        textPlaceholder = stringResource(id = string.fullname),
                        modifier = Modifier.weight(1f),
                        testTag = "name",
                    )
                }
            }
            PrimaryTextField(
                value = pass,
                onValueChange = onPassChange,
                textPlaceholder = stringResource(id = string.password),
                isPassWord = true,
                paddingValues = PaddingValues(
                    top = if (AppTheme.orientation.isLandscape()) 10.dp else 20.dp,
                ),
                modifier = null,
                testTag = "password",
            )
            Spacer(
                modifier = Modifier.size(
                    if (AppTheme.orientation.isLandscape()) 20.dp else 40.dp,
                ),
            )
            PrimaryButton(
                onClick = {
                    onSubmitRegister(User(name, email, pass))
                },
                label = stringResource(id = string.register),
                isEnable = !isInValid,
                modifier = Modifier.testTag("btn_register"),
            )
            Spacer(modifier = Modifier.weight(1f))
            if (AppTheme.orientation.isPortrait()) {
                Text(
                    text = stringResource(id = string.login).uppercase(),
                    style = AppTheme.styles.bodyLarge.copy(
                        textDecoration = TextDecoration.Underline,
                    ),
                    fontWeight = FontWeight.W500,
                    color = AppTheme.colors.onPrimary,
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                onNavigateBack()
                            })
                        },
                )
            }
        }
    }
}

@Composable
@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL,
    device = "spec:orientation=landscape,width=411dp,height=891dp",
)
private fun Preview() {
    MovieTMDBTheme {
        RegisterContent(
            email = "",
            name = "",
            pass = "",
            isInValid = false,
            isTextFieldFocused = false,
            onUpdateTextFiledFocus = {},
            onEmailChange = {},
            onNameChange = {},
            onPassChange = {},
            onSubmitRegister = {},
            onNavigateBack = {},
        )
    }
}
