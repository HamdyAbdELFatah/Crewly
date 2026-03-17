plugins {
    id("com.android.library")
}

android {
    namespace = "com.madar.crewly.core.domain"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlin.test)
}
