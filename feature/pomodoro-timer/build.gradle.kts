plugins {
    id("stopwatch.lint")
    id("stopwatch.android.feature")
}

android {
    namespace = "com.timers.stopwatch.feature.pomodoro.timer"
}
dependencies {
    implementation(libs.ssp)
    implementation(libs.sdp)
    implementation(projects.feature.pomodoroBase)
}
