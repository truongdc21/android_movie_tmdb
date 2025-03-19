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
package com.truongdc.movie.feature.movieList

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.truongdc.movie.core.data.repository.MovieRepository
import com.truongdc.movie.core.data.result.asResult
import com.truongdc.movie.core.model.Movie
import com.truongdc.movie.core.state.CombinedStateDelegateImpl
import com.truongdc.movie.core.state.asyncUpdateInternalState
import com.truongdc.movie.core.viewmodel.CombinedStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
) : CombinedStateViewModel<MovieListViewModel.UiState, MovieListViewModel.State, MovieListViewModel.Event>(
    combinedStateDelegate = CombinedStateDelegateImpl(
        initialState = State(),
        initialUiState = UiState(),
    ),
) {

    data class UiState(
        val flowPagingMovie: Flow<PagingData<Movie>>? = null,
        val lazyGridState: LazyGridState = LazyGridState(),
    )

    data class State(val flowPagingMovie: Flow<PagingData<Movie>>? = null)

    sealed interface Event

    init {
        requestMovie()
        collectUpdateUiState(viewModelScope) { state, uiState ->
            uiState.copy(flowPagingMovie = state.flowPagingMovie?.cachedIn(viewModelScope))
        }
    }

    private fun requestMovie() = launchSafeTask {
        val mFlowPagingMovie = movieRepository.fetchMovies().asResult()
        asyncUpdateInternalState { state -> state.copy(flowPagingMovie = mFlowPagingMovie) }
    }
}
