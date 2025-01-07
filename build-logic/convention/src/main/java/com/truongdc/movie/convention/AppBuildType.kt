package com.truongdc.movie.convention

/**
 * This is shared between :app and :benchmarks module to provide configurations type safety.
 */
enum class AppBuildType(
    val applicationIdSuffix: String? = null,
    val isMinifyEnabled: Boolean,
    val isShrinkResources: Boolean,
    val isDebuggable: Boolean,
) {
    DEBUG(".debug", false, false, true),
    RELEASE(null, true, true, false),
}
