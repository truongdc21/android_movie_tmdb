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
package com.truongdc.movie.macrobenchmark.movies

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import androidx.test.uiautomator.untilHasChildren
import com.truongdc.movie.macrobenchmark.flingElementDownUp
import com.truongdc.movie.macrobenchmark.waitAndFindObject

fun MacrobenchmarkScope.movieListWaitForContent() {
    device.apply {
        wait(Until.gone(By.res("loading_content")), 5_000)
        val obj = waitAndFindObject(By.res("movie_list:movies"), 10_000)
        obj.wait(untilHasChildren(), 60_000)
    }
}

fun MacrobenchmarkScope.movieListScrollDownUp() {
    device.wait(Until.hasObject(By.res("movie_list:movies")), 5_000)
    val topicsList = device.findObject(By.res("movie_list:movies"))
    device.flingElementDownUp(topicsList)
}
