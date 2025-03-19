/*
 * Designed and developed by 2025 truongdc21 (Dang Chi Truong)
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
package com.truongdc.movie.core.common.exception.base

import kotlinx.coroutines.CompletableDeferred

data class AppExceptionWrapper(
    val appException: AppException,
    val exceptionCompleter: CompletableDeferred<Unit>? = null,
    val doOnRetry: (suspend () -> Unit)? = null,
    val overrideMessage: String? = null,
) {
    override fun toString(): String {
        return "AppExceptionWrapper(appException=$appException, exceptionCompleter=$exceptionCompleter, doOnRetry=$doOnRetry, overrideMessage=$overrideMessage)"
    }
}
