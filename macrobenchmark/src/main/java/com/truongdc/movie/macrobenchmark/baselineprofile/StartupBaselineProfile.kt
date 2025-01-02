package com.truongdc.movie.macrobenchmark.baselineprofile

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.junit4.BaselineProfileRule
import com.truongdc.movie.macrobenchmark.PACKAGE_NAME
import com.truongdc.movie.macrobenchmark.startActivityAndAllowNotifications
import org.junit.Rule
import org.junit.Test

class StartupBaselineProfile {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collect(
        PACKAGE_NAME,
        includeInStartupProfile = true,
        profileBlock = MacrobenchmarkScope::startActivityAndAllowNotifications,
    )
}
