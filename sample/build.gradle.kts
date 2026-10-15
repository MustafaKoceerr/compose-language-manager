plugins {
    alias(libs.plugins.languagemanager.android.application)
    alias(libs.plugins.languagemanager.android.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.mustafakoceerr.languagemanager.sample"

    defaultConfig {
        applicationId = "com.mustafakoceerr.languagemanager.sample"
        versionCode = 1
        versionName = "1.0"
    }

    androidResources {
        // Exposes the app's languages in system settings on API 33+ (reads res/resources.properties).
        generateLocaleConfig = true
    }
}

dependencies {
    implementation(project(":language-manager-compose"))
    implementation(project(":language-manager-hilt"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)
    ksp(libs.hilt.compiler)

    testImplementation(project(":language-manager-testing"))
    testImplementation(libs.junit)
    testImplementation(libs.truth)
}
