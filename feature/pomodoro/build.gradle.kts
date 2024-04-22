plugins {
    id("stopwatch.lint")
    id("stopwatch.android.feature")
}

android {
    namespace = "com.timers.stopwatch.feature.pomodoro"
    buildFeatures.viewBinding = true
}
dependencies {
    implementation(projects.core.commonAndroid)
    implementation(projects.feature.pomodoroBase)
    implementation(projects.feature.pomodoroInitial)
    implementation(projects.feature.pomodoroTimer)
    implementation(projects.feature.pomodoroResult)
}
