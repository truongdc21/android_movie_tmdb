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
package com.truongdc.movie.core.data.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.truongdc.movie.core.common.constant.Constants
import com.truongdc.movie.core.common.di.annotations.IoDispatcher
import com.truongdc.movie.core.data.paging.MoviePagingSource
import com.truongdc.movie.core.data.repository.MovieRepository
import com.truongdc.movie.core.data.result.ExecuteResult
import com.truongdc.movie.core.network.model.asExternalModel
import com.truongdc.movie.core.network.source.MovieNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieNetWork: MovieNetworkDataSource,
    @IoDispatcher dispatcherIO: CoroutineDispatcher,
) : ExecuteResult(dispatcherIO), MovieRepository {
    override suspend fun fetchMovies() = withResultContext {
        Pager(
            config = PagingConfig(pageSize = Constants.MAX_PAGE_SIZE, prefetchDistance = 2),
            pagingSourceFactory = {
                MoviePagingSource(movieNetWork)
            },
        ).flow
    }

    override suspend fun fetchDetailMovies(movieId: Int) = withResultContext {
        movieNetWork.fetchMovieDetail(movieId).asExternalModel()
    }
}
