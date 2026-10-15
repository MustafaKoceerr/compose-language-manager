plugins {
    alias(libs.plugins.languagemanager.android.library)
    alias(libs.plugins.languagemanager.publish)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.mustafakoceerr.languagemanager.hilt"
}

dependencies {
    api(project(":language-manager"))
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
