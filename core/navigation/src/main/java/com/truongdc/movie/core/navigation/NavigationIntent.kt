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

import android.widget.Toast

/**
 * A sealed class representing navigation
 * intents that can be sent to the navigator.
 */
sealed class NavigationIntent {

    /**
     * Represents an intent to display a toast message.
     */
    data class DisplayToast(
        val message: String,
        val duration: Int = Toast.LENGTH_SHORT,
    ) : NavigationIntent()

    /**
     * Represents an intent to display a snackbar message.
     */
    data class DisplaySnackBar(
        val message: String,
        val actionLabel: String? = null,
        val withDismissAction: Boolean = false,
    ) : NavigationIntent()

    /**
     * Represents an intent to toggle the settings dialog visibility.
     */
    data class ToggleSettingsDialog(
        val isVisible: Boolean,
    ) : NavigationIntent()

    /**
     * Represents an intent to toggle the force update dialog visibility.
     */
    data class ToggleForceUpdateDialog(
        val isVisible: Boolean,
    ) : NavigationIntent()

    /**
     * Represents an intent to toggle the logout dialog visibility.
     */
    data class ToggleLogoutDialog(
        val isVisible: Boolean,
    ) : NavigationIntent()

    /**
     * Represents an intent to toggle the maintenance mode dialog visibility.
     */
    data class ToggleMaintenanceDialog(
        val isVisible: Boolean,
    ) : NavigationIntent()

    /**
     * Represents an intent to toggle the error dialog visibility.
     */
    data class ToggleErrorDialog(
        val isVisible: Boolean,
    ) : NavigationIntent()
}
