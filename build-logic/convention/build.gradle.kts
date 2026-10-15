import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.mustafakoceerr.languagemanager.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = libs.plugins.languagemanager.android.application.get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.languagemanager.android.library.get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = libs.plugins.languagemanager.android.compose.get().pluginId
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("robolectric") {
            id = libs.plugins.languagemanager.robolectric.get().pluginId
            implementationClass = "RobolectricConventionPlugin"
        }
        register("publish") {
            id = libs.plugins.languagemanager.publish.get().pluginId
            implementationClass = "PublishConventionPlugin"
        }
    }
}
