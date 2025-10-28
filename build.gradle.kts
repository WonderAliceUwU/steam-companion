plugins {
    kotlin("multiplatform") version "2.1.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.21" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.21" apply false
    id("org.jetbrains.compose") version "1.9.1" apply false
    id("com.android.application") version "8.13.0" apply false
    id("com.android.kotlin.multiplatform.library") version "8.13.0" apply false
    id("com.android.lint") version "8.13.0" apply false
}

allprojects {
    group = "com.steamcompanion"
    version = "0.1.0"
}
