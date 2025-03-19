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
import com.truongdc.movie.core.common.exception.base.AppException
import com.truongdc.movie.core.common.exception.base.AppExceptionType

data class RemoteException(
    val kind: RemoteExceptionKind,
    val httpErrorCode: Int? = null,
    val serverError: ServerError? = null,
    val rootException: Throwable? = null,
) : AppException(AppExceptionType.REMOTE) {

    private val generalServerStatusCode: Int
        get() = serverError?.generalServerStatusCode
            ?: serverError?.errors?.firstOrNull()?.serverStatusCode
            ?: -1

    val generalServerErrorId: String?
        get() = serverError?.generalServerErrorId
            ?: serverError?.errors?.firstOrNull()?.serverErrorId

    val generalServerMessage: String?
        get() = serverError?.generalMessage
            ?: serverError?.errors?.firstOrNull()?.message

    override fun toString(): String {
        return """RemoteException: {
            kind: $kind
            httpErrorCode: $httpErrorCode,
            serverError: $serverError,
            rootException: $rootException,
            generalServerMessage: $generalServerMessage,
            generalServerStatusCode: $generalServerStatusCode,
            generalServerErrorId: $generalServerErrorId,
            stackTrace: ${rootException?.stackTrace ?: ""}
        }"""
    }
}

enum class RemoteExceptionKind {
    NETWORK,
    SERVER,
    UNKNOWN,
    HTTP,
}
