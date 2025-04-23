plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid) version "1.9.10"
    alias(libs.plugins.apollo)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" // this version matches your Kotlin version
    id("org.jetbrains.kotlin.plugin.serialization")

}


android {
    namespace = "com.cornellappdev.score"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.cornellappdev.score"
        minSdk = 26
        targetSdk = 34
        versionCode = 3
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    buildTypes {
        debug {
            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://score-dev.cornellappdev.com/graphql\""
            )
        }
        release {
            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://score-backend.cornellappdev.com/graphql\""
            )
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Jetpack Compose dependencies
    implementation(platform("androidx.compose:compose-bom:2024.01.00"))
    implementation("androidx.compose.ui:ui:1.4.0")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")
    implementation("androidx.activity:activity-compose")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
    implementation("androidx.navigation:navigation-compose:2.8.2")
    implementation(libs.material3)
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("com.google.accompanist:accompanist-pager:0.24.0-alpha")

    // Other dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation("io.coil-kt:coil-compose:2.0.0")
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.runtime.android)
    testImplementation(libs.junit)
    debugImplementation("androidx.compose.ui:ui-tooling")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.apollo.runtime)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    implementation(libs.apollo.runtime)
    implementation("io.coil-kt.coil3:coil-compose:3.1.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")
    lintChecks(libs.compose.lint.checks)
}

apollo {
    service("service") {
        packageName.set("com.example.score")
        introspection {
            endpointUrl.set("https://score.cornellappdev.com/graphql")
            schemaFile.set(file("src/main/graphql/schema.graphqls"))
        }
    }
}