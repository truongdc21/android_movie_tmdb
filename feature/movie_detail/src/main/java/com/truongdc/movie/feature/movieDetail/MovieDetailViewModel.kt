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
package com.truongdc.movie.feature.movieDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.truongdc.movie.core.data.repository.MovieRepository
import com.truongdc.movie.core.data.result.asResult
import com.truongdc.movie.core.model.Movie
import com.truongdc.movie.core.state.UiStateDelegateImpl
import com.truongdc.movie.core.viewmodel.UiStateViewModel
import com.truongdc.movie.feature.movieDetail.navigation.MovieDetailRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val stateHandle: SavedStateHandle,
) : UiStateViewModel<MovieDetailViewModel.UiState, MovieDetailViewModel.Event>(
    UiStateDelegateImpl(
        UiState(),
    ),
) {
    init {
        asyncUpdateUiState(viewModelScope) { state ->
            state.copy(movieId = MovieDetailRouter.getMovie(stateHandle)?.id ?: -1)
        }
        if (uiState.movieId != -1) {
            requestMovie(uiState.movieId)
        }
    }

    data class UiState(
        val movieId: Int = -1,
        val movie: Movie? = null,
    )

    sealed interface Event

    fun requestMovie(movieId: Int) = launchSafeTask(maxRetries = 3) {
        val movie = movieRepository.fetchDetailMovies(movieId).asResult()
        asyncUpdateUiState(viewModelScope) { state -> state.copy(movie = movie) }
    }
}
