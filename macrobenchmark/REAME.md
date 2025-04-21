# 📊 Optimizing Jetpack Compose Performance with MacroBenchmark & BaselineProfile

This document provides a detailed guide on setting up and using **MacroBenchmark** and **BaselineProfile** to measure and improve performance in Android applications built with **Jetpack Compose**.

---

## 🌟 Objectives

- Measure startup time and user interactions.
- Optimize **startup performance** using Baseline Profiles.
- Detect and fix jank in Compose UI.

---

## 1. Introduction

### What is MacroBenchmark?
A library that helps measure the real-world performance of Android apps using metrics such as:
- Startup time
- Scroll performance (jank)
- Frame timing

### What is a Baseline Profile?
A set of rules that pre-compiles (AOT) important code paths to improve runtime performance on real devices.

---

## 2. Setting up a Benchmark Project

### Add a benchmark module
Create a new module named `benchmark` (or any name you prefer).

#### Add dependencies:
```kotlin
// benchmark/build.gradle.kts
dependencies {
    implementation("androidx.benchmark:benchmark-macro-junit4:1.2.0")
    implementation("androidx.profileinstaller:profileinstaller:1.3.1")
}
```

#### AndroidManifest.xml
```xml
<application android:debuggable="true" />
```

#### build.gradle.kts
```kotlin
android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    testBuildType = "release"
    buildTypes {
        release {
            signingConfig = signingConfigs.debug
            debuggable = true
        }
    }
}
```

---

## 3. Writing MacroBenchmark Tests

### Measure Startup Time:
```kotlin
@RunWith(AndroidJUnit4::class)
class StartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startupCold() = benchmarkRule.measureRepeated(
        packageName = "com.example.app",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD,
        setupBlock = {
            pressHome()
        }
    ) {
        startActivityAndWait()
    }
}
```

### Measure Scroll Jank:
```kotlin
@Test
fun scrollFeed() = benchmarkRule.measureRepeated(
    packageName = "com.example.app",
    metrics = listOf(FrameTimingMetric()),
    iterations = 3,
    setupBlock = {
        startActivityAndWait()
    }
) {
    device.swipe(500, 1800, 500, 300, 30)
}
```

---

## 4. Creating and Using a Baseline Profile

### Step 1: Write a profile generator
```kotlin
@ExperimentalBaselineProfilesApi
@BaselineProfile(
    packageName = "com.example.app",
    profileBlock = {
        startActivityAndWait()
        // Important user interactions to capture profile
    }
)
class BaselineProfileGenerator
```

### Step 2: Configure app to use profile
```kotlin
android {
    buildTypes {
        release {
            baselineProfile {
                from(project(":benchmark"))
            }
        }
    }
}
```

---

## 5. Review Results

- Rerun benchmark tests to measure startup time after applying BaselineProfile.
- Use `adb logcat` or Android Studio Profiler to view `StartupTimingMetric` logs.
- Compare results before and after using the profile.

---

## ✅ Conclusion

| Technique           | Benefit                                               |
|--------------------|--------------------------------------------------------|
| MacroBenchmark     | Measures real-world performance, easy to debug & CI/CD |
| BaselineProfile    | Reduces startup time, improves user experience         |

Combining these tools is one of the most effective ways to ensure your Jetpack Compose app is smooth and fast in production.

---

## 📋 References

- [Macrobenchmark - Android Dev](https://developer.android.com/benchmark/macrobenchmark)
- [Baseline Profiles](https://developer.android.com/topic/performance/baselineprofiles)
- [Jetpack Compose Performance](https://developer.android.com/jetpack/compose/performance)

