plugins {
    id("stopwatch.lint")
    id("stopwatch.android.library")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.timers.stopwatch.core.common.android"

    buildFeatures.viewBinding = true
}

dependencies {
    api(libs.androidx.navigation.fragment.ktx)
    api(libs.androidx.navigation.ui.ktx)
    api(libs.material)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.ssp)
    implementation(libs.sdp)
    api(libs.androidx.constraintlayout)

    implementation(projects.core.model)
    implementation(projects.core.log)
}
