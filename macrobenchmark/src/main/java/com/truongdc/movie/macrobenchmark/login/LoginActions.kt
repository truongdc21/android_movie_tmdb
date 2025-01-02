package com.truongdc.movie.macrobenchmark.login

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import androidx.test.uiautomator.untilHasChildren
import com.truongdc.movie.macrobenchmark.waitAndFindObject

fun MacrobenchmarkScope.loginWaitForContent() {
    device.wait(Until.gone(By.res("loading_content")), 5_000)
    val obj = device.waitAndFindObject(By.res("login_content"), 10_000)
    obj.wait(untilHasChildren(), 60_000)
}

fun MacrobenchmarkScope.login() {
    device.findObject(By.res("email")).text = "truongdc"
    device.findObject(By.res("password")).text = "123456"
    device.findObject(By.res("btn_login")).click()
    device.waitForIdle()
}
