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

plugins {
    alias(libs.plugins.movieTMDB.android.application)
    alias(libs.plugins.movieTMDB.android.application.compose)
    alias(libs.plugins.movieTMDB.android.application.flavors)
    alias(libs.plugins.movieTMDB.android.application.jacoco)
    alias(libs.plugins.movieTMDB.hilt)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.kotlin.serialization)
    //id("movie_tmdb.android.application.firebase")
    //id("com.google.android.gms.oss-licenses-plugin")
}

android {
    defaultConfig {
        applicationId = Configs.APP_ID
        versionCode = Configs.VERSION_CODE
        versionName = Configs.VERSION_NAME
        // Custom test runner to set up Hilt dependency graph
        testInstrumentationRunner = Configs.ANDROID_JUNIT_RUNNER
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            // Set [isMinifyEnabled] and [isShrinkResources] to false for testing benchmark
            // because have some thing errors with proguard - R8
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile(Configs.PROGUARD_FILE), Configs.PROGUARD_RULES)
            signingConfig = signingConfigs.getByName("debug")
            baselineProfile.automaticGenerationDuringBuild = true
        }
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    namespace = Configs.NAMESPACE
}

dependencies {
    implementation(projects.benchmarkable)
    implementation(projects.feature.login)
    implementation(projects.feature.register)
    implementation(projects.feature.movieList)
    implementation(projects.feature.movieDetail)
    implementation(projects.feature.settings)

    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)
    implementation(projects.core.viewmodel)
    implementation(projects.core.ui)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.window.core)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.coil.kt)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)

    ksp(libs.hilt.compiler)
    kspTest(libs.hilt.compiler)

    debugImplementation(libs.androidx.compose.ui.testManifest)

    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.kotlin.test)
    testImplementation(projects.core.testing)

    testDevelopImplementation(libs.robolectric)
    testDevelopImplementation(libs.roborazzi)

    androidTestImplementation(kotlin("test"))
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(projects.core.testing)

    baselineProfile(projects.macrobenchmark)
}

baselineProfile {
    // Don't build on every iteration of a full assemble.
    // Instead enable generation directly for the release build variant.
    automaticGenerationDuringBuild = false

    // Make use of Dex Layout Optimizations via Startup Profiles
    dexLayoutOptimization = true
}

//dependencyGuard at here