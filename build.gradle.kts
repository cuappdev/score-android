// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) version "1.9.10" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    id("com.google.gms.google-services") version "4.4.3" apply false
}
