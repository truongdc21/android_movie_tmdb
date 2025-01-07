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

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.truongdc.movie.core.designsystem.icons.AppIcons
import com.truongdc.movie.core.designsystem.theme.AppTheme
import com.truongdc.movie.core.designsystem.theme.MovieTMDBTheme
import com.truongdc.movie.core.ui.UiStateContent
import com.truongdc.movie.core.ui.extensions.showToast

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
) {
    val context = LocalContext.current
    val view = LocalView.current
    ObserverKeyBoard(view) { viewModel.onUpdateTextFiledFocus(it) }
    UiStateContent(uiStateDelegate = viewModel, modifier = Modifier, onEventEffect = { event ->
        when (event) {
            LoginViewModel.Event.LoginSuccess -> {
                context.showToast(context.getString(string.login_success))
                onLoginSuccess.invoke()
            }

            LoginViewModel.Event.LoginFailed -> {
                context.showToast(context.getString(string.login_failed_please_try_again))
            }
        }
    }, content = { uiState ->
        LoginContent(
            email = uiState.email,
            pass = uiState.pass,
            inValid = uiState.isInValid,
            isTextFieldFocused = uiState.isTextFieldFocused,
            onEmailChange = viewModel::onEmailChange,
            onPassChange = viewModel::onPassChange,
            onSubmitLogin = viewModel::onSubmitLogin,
            onNavigateRegister = onNavigateToRegister,
            onForgetPassword = {},
        )
    })
}

@Composable
private fun LoginContent(
    email: String,
    pass: String,
    inValid: Boolean,
    isTextFieldFocused: Boolean,
    onEmailChange: (String) -> Unit,
    onPassChange: (String) -> Unit,
    onSubmitLogin: (String, String) -> Unit,
    onNavigateRegister: () -> Unit,
    onForgetPassword: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val paddingTopContent = if (AppTheme.orientation.isLandscape()) {
        10.dp
    } else {
        if (!isTextFieldFocused) 150.dp else 80.dp
    }
    val paddingHorizontalContent = if (AppTheme.orientation.isLandscape()) 230.dp else 24.dp
    Scaffold(
        floatingActionButton = {
            if (AppTheme.orientation.isLandscape()) {
                FloatingActionButton(
                    onClick = { onNavigateRegister() },
                    content = { Icon(AppIcons.Add, contentDescription = null) },
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .background(AppTheme.colors.primary)
                .fillMaxSize()
                .padding(it)
                .padding(
                    top = paddingTopContent,
                    start = paddingHorizontalContent,
                    end = paddingHorizontalContent,
                )
                .testTag("login_content"),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = string.welcome_back),
                style = AppTheme.styles.displaySmall,
                color = AppTheme.colors.onPrimary,

            )
            Text(
                text = stringResource(id = string.login_to_continue),
                style = AppTheme.styles.bodyLarge,
                color = AppTheme.colors.onPrimary,
            )
            PrimaryTextField(
                value = email,
                onValueChange = { email -> onEmailChange((email)) },
                textPlaceholder = stringResource(id = string.mail_id),
                paddingValues = PaddingValues(
                    top = if (AppTheme.orientation.isLandscape()) 10.dp else 50.dp,
                ),
                testTag = "email",
            )
            PrimaryTextField(
                value = pass,
                onValueChange = { pass -> onPassChange(pass) },
                textPlaceholder = stringResource(id = string.password),
                isPassWord = true,
                paddingValues = PaddingValues(
                    top = if (AppTheme.orientation.isLandscape()) 10.dp else 20.dp,
                ),
                testTag = "password",
            )
            Spacer(
                modifier = Modifier.size(
                    if (AppTheme.orientation.isLandscape()) 10.dp else 30.dp,
                ),
            )
            Text(
                text = stringResource(id = string.forget_password),
                style = AppTheme.styles.bodyLarge.copy(
                    textDecoration = TextDecoration.Underline,
                ),
                color = AppTheme.colors.onPrimary,
                modifier = Modifier.clickable { onForgetPassword() },
            )
            Spacer(
                modifier = Modifier.size(
                    if (AppTheme.orientation.isLandscape()) 10.dp else 30.dp,
                ),
            )
            PrimaryButton(
                label = stringResource(id = string.login),
                isEnable = !inValid,
                onClick = {
                    keyboardController?.hide()
                    onSubmitLogin(email, pass)
                },
                modifier = Modifier.testTag("btn_login"),
            )
            Spacer(modifier = Modifier.weight(1f))
            if (AppTheme.orientation.isPortrait()) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                onNavigateRegister()
                            })
                        }
                        .testTag("btn_create_account"),
                    text = stringResource(id = string.create_account).uppercase(),
                    style = AppTheme.styles.bodyLarge.copy(
                        textDecoration = TextDecoration.Underline,
                    ),
                    fontWeight = FontWeight.W500,
                    color = AppTheme.colors.onPrimary,
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL,
    device = "spec:orientation=portrait,width=411dp,height=891dp",
)
@Composable
private fun Preview() {
    MovieTMDBTheme {
        LoginContent(
            email = "",
            pass = "",
            inValid = false,
            isTextFieldFocused = false,
            onEmailChange = {},
            onPassChange = {},
            onSubmitLogin = { _, _ -> },
            onNavigateRegister = {},
            onForgetPassword = {},
        )
    }
}
