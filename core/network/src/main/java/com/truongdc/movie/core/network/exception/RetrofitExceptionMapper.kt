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
import com.truongdc.movie.core.common.exception.base.ExceptionMapper
import com.truongdc.movie.core.network.error.ErrorType
import com.truongdc.movie.core.network.error.RetrofitException

class RetrofitExceptionMapper : ExceptionMapper<RemoteException>() {
    override fun map(exception: Any?): RemoteException {
        if (exception is RemoteException) {
            return exception
        }
        if (exception is RetrofitException) {
            return when (exception.errorType) {
                ErrorType.SERVER -> {
                    RemoteException(
                        kind = RemoteExceptionKind.SERVER,
                        httpErrorCode = exception.httpCode,
                        rootException = exception.cause,
                        serverError = ServerError(
                            generalMessage = exception.errorResponse?.messages ?: "",
                        ),
                    )
                }

                ErrorType.NETWORK -> {
                    RemoteException(
                        kind = RemoteExceptionKind.NETWORK,
                        rootException = exception.cause,
                    )
                }

                ErrorType.HTTP -> {
                    RemoteException(
                        kind = RemoteExceptionKind.HTTP,
                        rootException = exception.cause,
                    )
                }

                else -> {
                    RemoteException(
                        kind = RemoteExceptionKind.UNKNOWN,
                        rootException = exception.cause,
                    )
                }
            }
        }
        return RemoteException(
            kind = RemoteExceptionKind.UNKNOWN,
            rootException = exception as? Throwable,
        )
    }
}
