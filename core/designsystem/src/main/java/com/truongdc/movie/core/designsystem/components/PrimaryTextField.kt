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
package com.truongdc.movie.core.designsystem.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.truongdc.movie.core.designsystem.dimens.rememberWindowSizeClass
import com.truongdc.movie.core.designsystem.theme.AppTheme
import com.truongdc.movie.core.designsystem.theme.MovieTMDBTheme

@Composable
fun PrimaryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    textPlaceholder: String? = null,
    isPassWord: Boolean = false,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    paddingValues: PaddingValues = PaddingValues(),
    trailingIcon: @Composable (() -> Unit)? = null,
    primaryColor: Color? = null,
    onPrimaryColor: Color? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier? = null,
    testTag: String? = null,
) {
    TextField(
        modifier = (modifier ?: Modifier).testTag(testTag ?: "")
            .fillMaxWidth()
            .padding(paddingValues),
        value = value,
        readOnly = readOnly,
        onValueChange = onValueChange,
        textStyle = AppTheme.styles.bodyLarge,
        placeholder = {
            textPlaceholder?.let {
                Text(
                    text = it,
                    color = AppTheme.colors.onPrimary,
                    style = AppTheme.styles.bodyLarge,
                )
            }
        },
        maxLines = maxLines,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = primaryColor ?: AppTheme.colors.primary,
            focusedContainerColor = primaryColor ?: AppTheme.colors.primary,
            unfocusedIndicatorColor = onPrimaryColor ?: AppTheme.colors.onPrimary,
            focusedIndicatorColor = onPrimaryColor ?: AppTheme.colors.onPrimary,
            disabledIndicatorColor = onPrimaryColor ?: AppTheme.colors.onPrimary,
            focusedTextColor = onPrimaryColor ?: AppTheme.colors.onPrimary,
            unfocusedTextColor = onPrimaryColor ?: AppTheme.colors.onPrimary,
            cursorColor = onPrimaryColor ?: AppTheme.colors.onPrimary,
        ),
        visualTransformation = if (isPassWord) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
        ),
        trailingIcon = trailingIcon,
    )
}

@Preview
@Composable
private fun Preview() {
    MovieTMDBTheme(
        windowSizeClass = rememberWindowSizeClass(),
        androidTheme = true,
        darkTheme = false,
    ) {
        PrimaryTextField(
            value = "Input message",
            onValueChange = {},
            textPlaceholder = "",
            isPassWord = false,
            maxLines = 1,
            readOnly = false,
            paddingValues = PaddingValues(),
            trailingIcon = null,
            primaryColor = AppTheme.colors.surfaceContainer,
            onPrimaryColor = AppTheme.colors.onSurfaceVariant,
        )
    }
}
