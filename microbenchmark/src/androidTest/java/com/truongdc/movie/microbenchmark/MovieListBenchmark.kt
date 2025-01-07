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
package com.truongdc.movie.microbenchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.compose.material3.Scaffold
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.truongdc.movie.benchmarkable.MainActivity
import com.truongdc.movie.benchmarkable.MovieList
import com.truongdc.movie.core.model.Movie
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

@LargeTest
@RunWith(AndroidJUnit4::class)
class MovieListBenchmark {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val movieList = List(100) { index ->
        Movie(
            id = index,
            backDropImage = "",
            overView = "",
            vote = 0.0,
            voteCount = 0,
            title = "Movie $index",
            urlImage = "",
            originalTitle = "",
        )
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun moviesLazyColumRenderingBenchmark() {
        composeTestRule.setContent {
            Scaffold(
                modifier = Modifier.semantics {
                    testTagsAsResourceId = true
                },
            ) {
                MovieList(movies = movieList)
            }
        }
        benchmarkRule.measureRepeated {
            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithText("Movie 10").assertExists()
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun moviesLazyColumScrollToPosition_50_Benchmark() {
        composeTestRule.setContent {
            Scaffold(
                modifier = Modifier.semantics {
                    testTagsAsResourceId = true
                },
            ) {
                MovieList(movies = movieList)
            }
        }
        benchmarkRule.measureRepeated {
            composeTestRule.waitForIdle()
            composeTestRule.onNode(hasScrollAction()).performScrollToIndex(50)
            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithText("Movie 50").assertExists()
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun moviesLazyColumScrollToPosition_99_Benchmark() {
        composeTestRule.setContent {
            Scaffold(
                modifier = Modifier.semantics {
                    testTagsAsResourceId = true
                },
            ) {
                MovieList(movies = movieList)
            }
        }
        benchmarkRule.measureRepeated {
            composeTestRule.waitForIdle()
            composeTestRule.onNode(hasScrollAction()).performScrollToIndex(99)
            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithText("Movie 99").assertExists()
        }
    }
}
