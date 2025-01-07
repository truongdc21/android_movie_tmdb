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
plugins {
    alias(libs.plugins.movieTMDB.android.library)
    alias(libs.plugins.movieTMDB.android.library.compose)
    alias(libs.plugins.benchmark)
}

android {

    namespace = "com.truongdc.movie.microbenchmark"

    defaultConfig {
        // Set this argument to capture profiling information, instead of measuring performance.
        // Can be one of:
        //   * None
        //   * StackSampling
        //   * MethodTracing
        // See full descriptions of available options at: d.android.com/benchmark#profiling
        testInstrumentationRunnerArguments["androidx.benchmark.profiling.mode"] = "StackSampling"
    }

    testBuildType = "release"

    buildTypes {
        getByName("release") {
            // The androidx.benchmark plugin configures release buildType with proper settings, such as:
            // - disables code coverage
            // - adds CPU clock locking task
            // - signs release buildType with debug signing config
            // - copies benchmark results into build/outputs/connected_android_test_additional_output folder
        }
    }
}

dependencies {
    androidTestImplementation(libs.androidx.ui.test.junit4.android)
    androidTestImplementation(projects.benchmarkable)
    androidTestImplementation(libs.androidx.benchmark.micro)

    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.kotlin.stdlib)

    androidTestImplementation(libs.androidx.activity.compose)
    androidTestImplementation(projects.core.model)
    androidTestImplementation(projects.core.designsystem)
}
