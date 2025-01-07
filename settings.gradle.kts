pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "android_movie_tmdb"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// module apps
include(":app")

// module benchmarks
include(":macrobenchmark")
include(":microbenchmark")
include(":benchmarkable")

// module cores
include(":core:data")
include(":core:model")
include(":core:common")
include(":core:network")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:designsystem")
include(":core:navigation")
include(":core:state")
include(":core:viewmodel")
include(":core:ui")
include(":core:testing")

// module features
include(":feature:login")
include(":feature:register")
include(":feature:movie_list")
include(":feature:movie_detail")
include(":feature:settings")

// module others
include(":lint")

check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)) {
    """
    Movie app requires JDK 17+ but it is currently using JDK ${JavaVersion.current()}.
    Java Home: [${System.getProperty("java.home")}]
    https://developer.android.com/build/jdks#jdk-config-in-studio
    """.trimIndent()
}
