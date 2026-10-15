plugins {
    alias(libs.plugins.languagemanager.android.library)
    alias(libs.plugins.languagemanager.android.compose)
    alias(libs.plugins.languagemanager.publish)
    alias(libs.plugins.languagemanager.robolectric)
}

android {
    namespace = "com.mustafakoceerr.languagemanager.compose"
}

dependencies {
    api(project(":language-manager"))
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)

    testImplementation(project(":language-manager-testing"))
    testImplementation(libs.junit)
    testImplementation(libs.androidx.compose.foundation)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.truth)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
