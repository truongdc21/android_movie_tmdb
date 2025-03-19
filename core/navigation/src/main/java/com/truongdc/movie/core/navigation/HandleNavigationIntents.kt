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
package com.truongdc.movie.core.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * A composable function to handle navigation intents from `AppNavigator` and trigger UI actions.
 *
 * @param navigator The [AppNavigator] instance to observe navigation intents.
 * @param displaySnackBar A suspend function to show a snackbar with a message and optional action.
 * @param toggleSettingDialogVisibility Toggles the visibility of the settings dialog.
 * @param displayToastMessage Displays a toast message.
 * @param toggleForceUpdateDialog Toggles the visibility of the force update dialog.
 * @param toggleMaintenanceMode Toggles maintenance mode state.
 * @param toggleLogoutDialogVisibility Toggles the visibility of the logout dialog.
 *
 * Observes navigation intents like showing toasts, snackbars, or dialogs, and processes them
 * using the provided handlers, ensuring lifecycle safety.
 */
@Composable
fun HandleNavigationIntents(
    navigator: AppNavigator,
    displaySnackBar: suspend (String, String?, Boolean) -> Unit,
    toggleSettingDialogVisibility: (Boolean) -> Unit,
    displayToastMessage: (String) -> Unit,
    toggleForceUpdateDialog: (Boolean) -> Unit,
    toggleMaintenanceMode: (Boolean) -> Unit,
    toggleLogoutDialogVisibility: (Boolean) -> Unit,
    toggleErrorDialogVisibility: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val activity = (context as? Activity)
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(navigator, activity) {
        // Launches a coroutine in the lifecycle scope of the activity.
        lifecycleOwner.lifecycleScope.launch {
            // Observes the navigation channel for incoming navigation intents.
            navigator.navigationChannel.receiveAsFlow().collect { intent ->
                // Checks if the activity is still active before processing the intent.
                if (activity?.isFinishing == true) return@collect
                when (intent) {
                    is NavigationIntent.DisplayToast -> displayToastMessage(intent.message)

                    is NavigationIntent.DisplaySnackBar -> {
                        displaySnackBar(
                            intent.message,
                            intent.actionLabel,
                            intent.withDismissAction,
                        )
                    }

                    is NavigationIntent.ToggleSettingsDialog -> {
                        toggleSettingDialogVisibility(intent.isVisible)
                    }

                    is NavigationIntent.ToggleForceUpdateDialog -> {
                        toggleForceUpdateDialog(intent.isVisible)
                    }

                    is NavigationIntent.ToggleMaintenanceDialog ->
                        toggleMaintenanceMode(intent.isVisible)

                    is NavigationIntent.ToggleLogoutDialog -> {
                        toggleLogoutDialogVisibility(intent.isVisible)
                    }

                    is NavigationIntent.ToggleErrorDialog -> {
                        toggleErrorDialogVisibility(intent.isVisible)
                    }
                }
            }
        }
    }
}
