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

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.truongdc.movie.macrobenchmark.PACKAGE_NAME
import com.truongdc.movie.macrobenchmark.login.login
import com.truongdc.movie.macrobenchmark.login.loginWaitForContent
import com.truongdc.movie.macrobenchmark.register.goToRegisterScreen
import com.truongdc.movie.macrobenchmark.register.registerAccount
import com.truongdc.movie.macrobenchmark.register.registerWaitForContent
import com.truongdc.movie.macrobenchmark.startActivityAndAllowNotifications
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalMetricApi::class)
class ScrollMovieListBenchMark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun defaultCompilation() = scroll(CompilationMode.DEFAULT)

    @Test
    fun full() = scroll(CompilationMode.Full())

    private fun scroll(compilationMode: CompilationMode) {
        var isLogin = false
        benchmarkRule.measureRepeated(
            packageName = PACKAGE_NAME,
            metrics = listOf(
                TraceSectionMetric("ClickTrace"),
                StartupTimingMetric(),
                FrameTimingMetric(),
            ),
            compilationMode = compilationMode,
            startupMode = null,
            iterations = 5,
            setupBlock = {
                startActivityAndAllowNotifications()
                // check Login to skip setup flow register and login
                if (!isLogin) {
                    loginWaitForContent()
                    goToRegisterScreen()
                    registerWaitForContent()
                    registerAccount()
                    loginWaitForContent()
                    login()
                    isLogin = true
                }
            },
        ) {
            movieListWaitForContent()
            repeat(3) {
                movieListScrollDownUp()
            }
        }
    }
}
