plugins {
    alias(libs.plugins.languagemanager.android.library)
    alias(libs.plugins.languagemanager.publish)
}

android {
    namespace = "com.mustafakoceerr.languagemanager.testing"
}

dependencies {
    api(project(":language-manager"))

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)
    testImplementation(libs.turbine)
}
