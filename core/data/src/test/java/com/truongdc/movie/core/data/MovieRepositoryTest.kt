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
package com.truongdc.movie.core.data

import com.truongdc.movie.core.data.repository.impl.MovieRepositoryImpl
import com.truongdc.movie.core.data.result.DataResult
import com.truongdc.movie.core.data.testdoubles.TestMovieNetworkDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class MovieRepositoryTest {

    private val movieNetworkDataSource = TestMovieNetworkDataSource()

    private lateinit var repository: MovieRepositoryImpl

    private val testDispatcher = UnconfinedTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        repository = MovieRepositoryImpl(
            movieNetWork = movieNetworkDataSource,
            dispatcherIO = testDispatcher,
        )
    }

    @Test
    fun fetchMovies_should_success_() =
        testScope.runTest {
            val dataResult = repository.fetchMovies().map { it.firstOrNull() }
            assertTrue(dataResult is DataResult.Success)
        }

    @Test
    fun fetchMovieDetail_should_success_() =
        testScope.runTest {
            val dataResult = repository.fetchDetailMovies(1)
            assertTrue(dataResult is DataResult.Success)
        }

    @Test
    fun fetchMovieDetail_should_error_() =
        testScope.runTest {
            val dataResult = repository.fetchDetailMovies(4)
            assertTrue(dataResult is DataResult.Error)
        }
}
