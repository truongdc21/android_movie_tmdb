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
package com.truongdc.movie.macrobenchmark.login

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.truongdc.movie.macrobenchmark.PACKAGE_NAME
import com.truongdc.movie.macrobenchmark.clearAppData
import com.truongdc.movie.macrobenchmark.register.goToRegisterScreen
import com.truongdc.movie.macrobenchmark.register.registerAccount
import com.truongdc.movie.macrobenchmark.register.registerWaitForContent
import com.truongdc.movie.macrobenchmark.startActivityAndAllowNotifications
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun loginWithCompilationNone() {
        benchmarkLogin(
            compilationMode = CompilationMode.None(),
        )
    }

    @Test
    fun loginWithCompilationBaselineProfile() {
        benchmarkLogin(
            compilationMode = CompilationMode.Partial(baselineProfileMode = BaselineProfileMode.Require),
        )
    }

    private fun benchmarkLogin(compilationMode: CompilationMode) {
        benchmarkRule.measureRepeated(
            packageName = PACKAGE_NAME,
            metrics = listOf(StartupTimingMetric(), FrameTimingMetric()),
            compilationMode = compilationMode,
            startupMode = StartupMode.COLD,
            iterations = 5,
            setupBlock = {
                pressHome()
                clearAppData()
                startActivityAndAllowNotifications()
                loginWaitForContent()
                goToRegisterScreen()
                registerWaitForContent()
                registerAccount()
            },
            measureBlock = {
                startActivityAndAllowNotifications()
                loginWaitForContent()
                login()
            },
        )
    }
}
