plugins {
    alias(libs.plugins.languagemanager.android.library)
    alias(libs.plugins.languagemanager.publish)
    alias(libs.plugins.languagemanager.robolectric)
}

android {
    namespace = "com.mustafakoceerr.languagemanager"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    api(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)
    testImplementation(libs.turbine)
}
