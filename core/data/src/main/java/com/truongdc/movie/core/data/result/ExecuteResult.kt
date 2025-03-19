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
package com.truongdc.movie.core.data.result

import com.truongdc.movie.core.common.di.annotations.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class ExecuteResult(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    protected suspend fun <R> withResultContext(
        context: CoroutineContext = dispatcher,
        requestBlock: suspend CoroutineScope.() -> R,
    ): DataResult<R> = withContext(context) {
        return@withContext try {
            val response = requestBlock()
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }
}
