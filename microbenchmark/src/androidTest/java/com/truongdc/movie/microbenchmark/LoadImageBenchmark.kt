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
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.truongdc.movie.benchmarkable.LoadImageFromCache
import com.truongdc.movie.benchmarkable.LoadImageFromUrl
import com.truongdc.movie.benchmarkable.MainActivity
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class LoadImageBenchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val imageUrl =
        "https://media-blog.jobsgo.vn/blog/wp-content/uploads/2023/02/coder-la-gi.jpg"

    private lateinit var cachedImageRequest: ImageRequest

    @Before
    fun setup() {
        val context = composeTestRule.activity
        val imageLoader = ImageLoader(context)

        // Preload image to cache
        runBlocking {
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build()

            // Ensure the image is loaded and cached
            imageLoader.execute(request)

            // Store the cached request for reuse in the benchmark
            cachedImageRequest = request
        }
    }

    @Test
    @OptIn(ExperimentalComposeUiApi::class)
    fun benchmarkLoadImageFromUrl() {
        composeTestRule.setContent {
            Scaffold(
                modifier = Modifier.semantics {
                    testTagsAsResourceId = true
                },
            ) {
                LoadImageFromUrl(imageUrl = imageUrl)
            }
        }
        benchmarkRule.measureRepeated {
            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithTag("image_url").assertExists()
        }
    }

    @Test
    @OptIn(ExperimentalComposeUiApi::class)
    fun benchmarkLoadImageFromCache() {
        composeTestRule.setContent {
            Scaffold(
                modifier = Modifier.semantics {
                    testTagsAsResourceId = true
                },
            ) {
                LoadImageFromCache(imageRequest = cachedImageRequest)
            }
        }
        benchmarkRule.measureRepeated {
            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithTag("image_cache").assertExists()
        }
    }
}
