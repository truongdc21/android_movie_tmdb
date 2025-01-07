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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.truongdc.movie.core.designsystem.R.string
import com.truongdc.movie.core.designsystem.components.ErrorMessage
import com.truongdc.movie.core.designsystem.components.LoadingNextPageItem
import com.truongdc.movie.core.designsystem.components.PageLoader
import com.truongdc.movie.core.designsystem.dimens.Orientation
import com.truongdc.movie.core.designsystem.icons.AppIcons
import com.truongdc.movie.core.designsystem.theme.AppTheme
import com.truongdc.movie.core.model.Movie
import com.truongdc.movie.core.ui.TrackScrollJank
import com.truongdc.movie.core.ui.UiStateContent
import com.truongdc.movie.feature.movieList.components.MovieItem

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = hiltViewModel(),
    onNavigateToDetail: (Movie) -> Unit = {},
    onShowSettingDialog: () -> Unit = {},
) {
    UiStateContent(
        uiStateDelegate = viewModel,
        modifier = Modifier,
        onEventEffect = {},
    ) { uiState ->
        Scaffold(
            topBar = {
                if (AppTheme.orientation == Orientation.Portrait) {
                    MovieTopBar { onShowSettingDialog.invoke() }
                }
            },
            floatingActionButton = {
                if (AppTheme.orientation == Orientation.Landscape) {
                    FloatingActionButton(
                        onClick = { onShowSettingDialog.invoke() },
                        content = { Icon(AppIcons.Settings, contentDescription = null) },
                    )
                }
            },
        ) { paddingValues ->
            MoviesContent(
                paddingValues = paddingValues,
                uiState = uiState,
                onTapMovie = onNavigateToDetail,
            )
        }
    }
}

@Composable
private fun MoviesContent(
    paddingValues: PaddingValues,
    uiState: MovieListViewModel.UiState,
    onTapMovie: (Movie) -> Unit,
) {
    uiState.flowPagingMovie?.let { pagingData ->
        val pagingItems = pagingData.collectAsLazyPagingItems()
        val countColumns: Int = if (AppTheme.orientation.isPortrait()) {
            2
        } else {
            if (AppTheme.windowType.isGreaterThanCompact()) 5 else 4
        }
        Column(
            modifier = Modifier.fillMaxSize().testTag("movie_list:movies"),
        ) {
            TrackScrollJank(uiState.lazyGridState, "movie_list:screen")
            LazyVerticalGrid(
                state = uiState.lazyGridState,
                columns = GridCells.Fixed(countColumns),
                modifier = Modifier.padding(paddingValues),
            ) {
                items(pagingItems.itemCount) { index ->
                    pagingItems[index]?.let { item ->
                        MovieItem(
                            movie = item,
                            onClickItem = { movieId -> onTapMovie(movieId) },
                        )
                    }
                }
                buildLoadState(pagingItems)
            }
        }
    }
}

private fun LazyGridScope.buildLoadState(
    lazyPagingItems: LazyPagingItems<Movie>,
) {
    lazyPagingItems.apply {
        buildRefreshState(loadState.refresh) { retry() }
        buildAppendState(loadState.append) { retry() }
    }
}

private fun LazyGridScope.buildRefreshState(
    loadState: LoadState,
    onRetry: () -> Unit,
) {
    when (loadState) {
        is LoadState.Loading -> {
            item(span = { GridItemSpan(maxLineSpan) }) {
                PageLoader()
            }
        }

        is LoadState.Error -> {
            item(span = { GridItemSpan(maxLineSpan) }) {
                ErrorMessage(
                    message = loadState.error.localizedMessage ?: "Unknown error",
                    onClickRetry = onRetry,
                )
            }
        }

        else -> Unit
    }
}

private fun LazyGridScope.buildAppendState(
    loadState: LoadState,
    onRetry: () -> Unit,
) {
    when (loadState) {
        is LoadState.Loading -> {
            item(span = { GridItemSpan(maxLineSpan) }) {
                LoadingNextPageItem(modifier = Modifier)
            }
        }

        is LoadState.Error -> {
            item(span = { GridItemSpan(maxLineSpan) }) {
                ErrorMessage(
                    message = loadState.error.localizedMessage ?: "Unknown error",
                    onClickRetry = onRetry,
                )
            }
        }

        else -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieTopBar(showSetting: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = string.movie_app).uppercase(),
                style = AppTheme.styles.titleMedium,
                color = AppTheme.colors.onPrimary,
                textAlign = TextAlign.Center,
            )
        },
        actions = {
            Icon(
                AppIcons.Settings,
                contentDescription = null,
                tint = AppTheme.colors.onPrimary,
                modifier = Modifier
                    .padding(end = 24.dp)
                    .clickable {
                        showSetting.invoke()
                    },
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(AppTheme.colors.primary),
    )
}
