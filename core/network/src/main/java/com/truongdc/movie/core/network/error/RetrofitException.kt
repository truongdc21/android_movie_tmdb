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
package com.truongdc.movie.core.network.error

import com.squareup.moshi.JsonDataException
import com.truongdc.movie.core.network.provider.MoshiBuilderProvider
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.text.ParseException

class RetrofitException(
    val errorType: String,
    val response: Response<*>? = null,
    val errorResponse: ErrorResponse? = null,
    val httpCode: Int = 0,
    cause: Throwable? = null,
) : RuntimeException(cause?.message, cause) {
    companion object {
        private const val TAG = "RetrofitException"
        private fun toHttpError(response: Response<*>, httpCode: Int) =
            RetrofitException(
                errorType = ErrorType.HTTP,
                response = response,
                httpCode = httpCode,
            )

        private fun toNetworkError(cause: Throwable) =
            RetrofitException(
                errorType = ErrorType.NETWORK,
                cause = cause,
            )

        private fun toServerError(
            errorResponse: ErrorResponse?,
            httpCode: Int,
            response: Response<*>?,
        ) =
            RetrofitException(
                errorType = ErrorType.SERVER,
                errorResponse = errorResponse,
                httpCode = httpCode,
                response = response,
            )

        private fun toUnexpectedError(cause: Throwable) =
            RetrofitException(
                errorType = ErrorType.UNEXPECTED,
                cause = cause,
            )

        fun convertToRetrofitException(throwable: Throwable): RetrofitException {
            if (throwable is RetrofitException) {
                return throwable
            }

            if (throwable is IOException) {
                return toNetworkError(cause = throwable)
            }

            if (throwable is HttpException) {
                val response = throwable.response() ?: return toUnexpectedError(
                    cause = throwable,
                )
                response.errorBody()?.let {
                    return try {
                        val moshi = MoshiBuilderProvider.moshiBuilder.build()
                        val adapter = moshi.adapter(ErrorResponse::class.java)
                        val errorResponse = adapter.fromJson(it.string())
                        if (errorResponse != null && !errorResponse.messages.isNullOrBlank()) {
                            toServerError(
                                errorResponse = errorResponse,
                                httpCode = response.code(),
                                response = response,
                            )
                        } else {
                            toHttpError(
                                response = response,
                                httpCode = response.code(),
                            )
                        }
                    } catch (e: IOException) {
                        Timber.tag(TAG).e(e.message.toString())
                        toUnexpectedError(cause = throwable)
                    } catch (e: ParseException) {
                        Timber.tag(TAG).e(e.message.toString())
                        toUnexpectedError(cause = throwable)
                    } catch (e: JsonDataException) {
                        Timber.tag(TAG).e(e.message.toString())
                        toUnexpectedError(cause = throwable)
                    }
                }
                return toHttpError(response = response, httpCode = response.code())
            }
            return toUnexpectedError(cause = throwable)
        }
    }
}
