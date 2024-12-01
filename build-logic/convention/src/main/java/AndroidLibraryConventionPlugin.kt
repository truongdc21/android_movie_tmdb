import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import com.truongdc.movie_tmdb.convention.configureFlavors
import com.truongdc.movie_tmdb.convention.configureGradleManagedDevices
import com.truongdc.movie_tmdb.convention.configureKotlinAndroid
import com.truongdc.movie_tmdb.convention.configurePrintApksTask
import com.truongdc.movie_tmdb.convention.disableUnnecessaryAndroidTests
import com.truongdc.movie_tmdb.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("movie_tmdb.android.lint")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                testOptions.animationsDisabled = true
                configureFlavors(this)
                configureGradleManagedDevices(this)
                // The resource prefix is derived from the module name,
                // so resources inside ":core:module1" must be prefixed with "core_module1_"
                // don't apply for :core:designsystem module, because this module will share resource for all modules
                if (path != ":core:designsystem") {
                    resourcePrefix =
                        path.split("""\W""".toRegex()).drop(1).distinct()
                            .joinToString(separator = "_")
                            .lowercase() + "_"
                }

            }

            extensions.configure<LibraryAndroidComponentsExtension> {
                configurePrintApksTask(this)
                disableUnnecessaryAndroidTests(target)
            }

            dependencies {
                add("api", project(":core:common"))
                add("androidTestImplementation", kotlin("test"))
                add("testImplementation", kotlin("test"))
                add("implementation", libs.findLibrary("androidx.tracing.ktx").get())
            }
        }
    }
}
