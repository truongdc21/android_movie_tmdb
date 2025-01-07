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

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.truongdc.movie.core.common.constant.Constants
import com.truongdc.movie.core.designsystem.theme.AppTheme
import com.truongdc.movie.core.designsystem.theme.MovieTMDBTheme
import com.truongdc.movie.core.model.Movie
import com.truongdc.movie.core.ui.UiStateContent

@Composable
fun MovieDetailScreen(
    viewModel: MovieDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
) {
    UiStateContent(
        uiStateDelegate = viewModel,
        modifier = Modifier,
        onEventEffect = {},
        onDismissErrorDialog = onNavigateBack,
        content = { uiState ->
            uiState.movie?.let {
                MovieDetailContent(uiState.movie)
            }
        },
    )
}

@Composable
private fun MovieDetailContent(movie: Movie) {
    val isPortrait = AppTheme.orientation.isPortrait()

    if (isPortrait) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            MovieHeader(movie = movie, isPortrait = true)
            MovieDescription(movie = movie)
        }
    } else {
        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            MovieHeader(movie = movie, isPortrait = false)
            MovieDescription(movie = movie)
        }
    }
}

@Composable
private fun MovieHeader(movie: Movie, isPortrait: Boolean) {
    val boxModifier = if (isPortrait) {
        Modifier
            .fillMaxWidth()
            .height(400.dp)
    } else {
        Modifier
            .width(500.dp)
            .fillMaxHeight()
    }

    val imageModifier = if (isPortrait) {
        Modifier
            .fillMaxWidth()
            .height(330.dp)
    } else {
        Modifier
            .fillMaxHeight()
            .width(430.dp)
    }

    val circleImageAlignment = if (isPortrait) Alignment.BottomStart else Alignment.TopEnd

    Box(modifier = boxModifier) {
        BackDropMovieImage(
            urlImage = movie.backDropImage,
            modifier = imageModifier,
        )
        MovieCircleImage(
            urlImage = movie.urlImage,
            modifier = Modifier
                .size(140.dp)
                .padding(16.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White, CircleShape)
                .align(circleImageAlignment),
        )
    }
}

@Composable
private fun MovieDescription(movie: Movie) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        item { Text(movie.title, style = AppTheme.styles.titleMedium) }
        item { Spacer(modifier = Modifier.padding(top = 10.dp)) }
        item { Text(movie.overView, style = AppTheme.styles.bodyMedium) }
    }
}

@Composable
private fun MovieCircleImage(urlImage: String, modifier: Modifier) {
    Image(
        painter = rememberAsyncImagePainter(
            Constants.BASE_URL_IMAGE + urlImage,
        ),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier,
    )
}

@Composable
private fun BackDropMovieImage(urlImage: String, modifier: Modifier) {
    val painter = rememberAsyncImagePainter(
        Constants.BASE_URL_IMAGE + urlImage,
    )
    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
    )
}

@Composable
@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL,
    device = "spec:orientation=portrait,width=411dp,height=891dp",
)
private fun Preview() {
    MovieTMDBTheme {
        MovieDetailContent(
            movie = Movie(
                id = 1,
                backDropImage = "",
                overView = "",
                vote = 0.0,
                voteCount = 0,
                title = "Movie 1",
                urlImage = "",
                originalTitle = "",
            ),
        )
    }
}
