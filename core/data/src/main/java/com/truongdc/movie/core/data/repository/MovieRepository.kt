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
package com.truongdc.movie.core.data.repository

import androidx.paging.PagingData
import com.truongdc.movie.core.data.result.DataResult
import com.truongdc.movie.core.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun fetchMovies(): DataResult<Flow<PagingData<Movie>>>

    suspend fun fetchDetailMovies(movieId: Int): DataResult<Movie>
}
