import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose.compiler)
}

group = "io.github.jackbeback"
version = "1.0.0"

kotlin {
    jvm()
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Multiplatform dependencies
                implementation(libs.kotlin.test)
                implementation(compose.runtime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ui.android)
                implementation(libs.runtime.android)
                implementation(libs.foundation.android)
            }
        }
    }
}

android {
    namespace = "io.github.jackbeback.uikit"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    buildFeatures {
        compose = true
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(group.toString(), "library", version.toString())

    pom {
        name = "uikit"
        description = "Collection Of Jetpack Compose UI Components"
        inceptionYear = "2025"
        url = "https://github.com/kotlin/multiplatform-library-template/"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "jackbeback"
                name = "JackBeBack"
                url = "https://github.com/JackBeBack"
            }
        }
        scm {
            url = "https://github.com/JackBeBack/UIKit"
            connection = "smc:git:git://github.com:JackBeBack/UIKit.git"
            developerConnection = "scm:git:ssh://git@github.com:JackBeBack/UIKit.git"
        }
    }
}
