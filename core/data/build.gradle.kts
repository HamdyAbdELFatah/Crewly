plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.madar.crewly.core.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}



dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.room3.runtime)
    ksp(libs.androidx.room3.compiler)
    implementation(libs.androidx.sqlite.bundled)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(project(":core:common"))
    
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlin.test)
}
