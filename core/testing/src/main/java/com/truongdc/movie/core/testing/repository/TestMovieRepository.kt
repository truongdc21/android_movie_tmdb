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
package com.truongdc.movie.core.testing.repository

import androidx.paging.PagingData
import com.truongdc.movie.core.data.repository.MovieRepository
import com.truongdc.movie.core.data.result.DataResult
import com.truongdc.movie.core.model.Movie
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class TestMovieRepository : MovieRepository {
    /**
     * The backing hot flow for the list of movies for testing.
     */
    private val moviesFlow: MutableSharedFlow<List<Movie>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    /**
     * A test-only API to allow controlling the list of movies from tests.
     */
    fun sendMovies(movies: List<Movie>) {
        moviesFlow.tryEmit(movies)
    }

    override suspend fun fetchMovies(): DataResult<Flow<PagingData<Movie>>> {
        return DataResult.Success(
            moviesFlow.map { movies ->
                PagingData.from(movies)
            },
        )
    }

    override suspend fun fetchDetailMovies(movieId: Int): DataResult<Movie> {
        val movie = moviesFlow.replayCache
            .flatten()
            .find { it.id == movieId }
        return if (movie != null) {
            DataResult.Success(movie)
        } else {
            DataResult.Error(Exception("Movie not found"))
        }
    }
}
