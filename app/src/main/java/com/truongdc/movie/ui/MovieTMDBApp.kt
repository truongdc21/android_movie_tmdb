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
package com.truongdc.movie.ui

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.hilt.navigation.compose.hiltViewModel
import com.truongdc.movie.core.designsystem.R.string
import com.truongdc.movie.core.navigation.AppNavigator
import com.truongdc.movie.core.navigation.HandleNavigationIntents
import com.truongdc.movie.core.ui.UiStateContent
import com.truongdc.movie.core.ui.extensions.showToast
import com.truongdc.movie.feature.login.navigation.LoginRouter
import com.truongdc.movie.feature.movieList.navigation.MovieListRouter
import com.truongdc.movie.feature.setting.SettingsDialog
import com.truongdc.movie.navigation.MovieNavHost

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MovieTMDBApp(
    navigator: AppNavigator,
    viewModel: MovieTMDBAppViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val hasLaunched = rememberSaveable { mutableStateOf(false) }
    UiStateContent(
        uiStateDelegate = viewModel,
        modifier = Modifier,
        onEventEffect = { event ->
            when (event) {
                MovieTMDBAppViewModel.Event.LogoutSuccess -> {
                    navigator.navigateTo(
                        route = LoginRouter,
                        popUpToRoute = MovieListRouter,
                        inclusive = true,
                    )
                }
            }
        },
    ) { uiState ->
        if (uiState.isShowSettingDialog) {
            SettingsDialog(
                onLogout = {
                    viewModel.logout()
                },
                onDismiss = {
                    viewModel.toggleSettingDialog(false)
                },
            )
        }
        HandleNavigationIntents(
            navigator = navigator,
            displayToastMessage = { message ->
                context.showToast(message)
            },
            displaySnackBar = { message, actionLabel, withDismissAction ->
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                    withDismissAction = withDismissAction,
                )
            },
            toggleSettingDialogVisibility = viewModel::toggleSettingDialog,
            toggleForceUpdateDialog = {},
            toggleMaintenanceMode = {},
            toggleLogoutDialogVisibility = {},
        )
        if (!hasLaunched.value) {
            LaunchedEffect(Unit) {
                navigator.displaySnackBar(
                    message = context.getString(string.welcome_to_movie_app),
                    withDismissAction = true,
                    duration = 0,
                )
                hasLaunched.value = true
            }
        }
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.semantics {
                testTagsAsResourceId = true
            },
        ) {
            uiState.isLogin?.let { isLogin ->
                MovieNavHost(
                    navigator = navigator,
                    isLogin = isLogin,
                )
            }
        }
    }
}
