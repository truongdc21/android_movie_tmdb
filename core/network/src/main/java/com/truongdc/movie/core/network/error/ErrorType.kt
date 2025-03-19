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

import java.io.IOException

/**
 * Error type
 */
object ErrorType {
    /**
     * An [IOException] occurred while communicating to the server.
     */
    const val NETWORK = "NETWORK"

    /**
     * A non-2xx HTTP netWorkState code was received from the server.
     */
    const val HTTP = "HTTP"

    /**
     * A error server withScheduler code & message
     */
    const val SERVER = "SERVER"

    /**
     * An internal error occurred while attempting to execute a request. It is best practice to
     * re-throw this exception so your application crashes.
     */
    const val UNEXPECTED = "UNEXPECTED"
}
