import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose")
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.kotlin.native.cocoapods")
}

kotlin {
    jvmToolchain(21)

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    
    val xcf = XCFramework()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "composeApp"
            xcf.add(this)
        }
    }

    sourceSets {
        val ktorVersion = "3.3.1"
        val coroutines = "1.10.2"
        val serialization = "1.9.0"
        val napier = "2.7.1"

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation("io.ktor:ktor-client-core:$ktorVersion")
            implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
            implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
            implementation("io.github.aakira:napier:$napier")
            implementation("media.kamel:kamel-image:1.0.8")
        }
        androidMain.dependencies {
            implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines")
            // Compose Activity host for setContent and ComponentActivity
            implementation("androidx.activity:activity-compose:1.11.0")
            implementation("androidx.security:security-crypto:1.1.0")
        }
        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:$ktorVersion")
        }
    }

    cocoapods {
        summary = "Compose application framework"
        homepage = "empty"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "composeApp"
            isStatic = true
        }
    }
}

android {
    namespace = "com.steamcompanion"
    compileSdk = 36

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    defaultConfig {
        applicationId = "com.steamcompanion"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// Ensure Android JavaCompile tasks use JDK 21 even if source/target are 17
// This allows building on machines with only Java 21 installed
tasks.withType<JavaCompile>().configureEach {
    javaCompiler = javaToolchains.compilerFor {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}