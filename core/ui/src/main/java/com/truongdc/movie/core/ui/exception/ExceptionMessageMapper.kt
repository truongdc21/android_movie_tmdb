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
package com.truongdc.movie.core.ui.exception

import RemoteException
import com.truongdc.movie.core.common.exception.auth.AuthException
import com.truongdc.movie.core.common.exception.auth.AuthExceptionKind
import com.truongdc.movie.core.common.exception.base.AppException
import com.truongdc.movie.core.common.exception.base.AppExceptionType
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ExceptionMessageMapper {
    fun map(appException: AppException): String {
        return when (appException.appExceptionType) {
            AppExceptionType.REMOTE -> when ((appException as RemoteException).kind) {
                RemoteExceptionKind.NETWORK -> getNetworkErrorMessage(appException.rootException)
                RemoteExceptionKind.SERVER -> appException.generalServerMessage ?: "Server error"
                RemoteExceptionKind.UNKNOWN -> "Unknown error"
                RemoteExceptionKind.HTTP -> appException.httpErrorCode?.getHttpErrorMessage()
                    ?: "HTTP error"
            }

            AppExceptionType.PARSE -> "Parse error"
            AppExceptionType.REMOTE_CONFIG -> "Remote config error"
            AppExceptionType.UNCAUGHT -> "Uncaught error"
            AppExceptionType.VALIDATION -> "Validation error"
            AppExceptionType.FIRESTORE -> "Firestore error"
            AppExceptionType.AUTH -> when ((appException as AuthException).kind) {
                AuthExceptionKind.INVALID_EMAIL -> "Invalid email"
                AuthExceptionKind.INVALID_PASSWORD -> "Invalid password"
            }
        }
    }

    private fun getNetworkErrorMessage(throwable: Throwable?): String {
        if (throwable is SocketTimeoutException) {
            return throwable.message.toString()
        }

        if (throwable is UnknownHostException) {
            return throwable.message.toString()
        }

        if (throwable is IOException) {
            return throwable.message.toString()
        }

        return throwable?.message.toString()
    }

    private fun Int.getHttpErrorMessage(): String {
        if (this in HttpURLConnection.HTTP_MULT_CHOICE..HttpURLConnection.HTTP_USE_PROXY) {
            // Redirection
            return "It was transferred to a different URL. I'm sorry for causing you trouble"
        }
        if (this in HttpURLConnection.HTTP_BAD_REQUEST..HttpURLConnection.HTTP_UNSUPPORTED_TYPE) {
            // Client error
            return "An error occurred on the application side. Please try again later!"
        }
        if (this in HttpURLConnection.HTTP_INTERNAL_ERROR..HttpURLConnection.HTTP_VERSION) {
            // Server error
            return "A server error occurred. Please try again later!"
        }

        // Unofficial error
        return "An error occurred. Please try again later!"
    }
}
