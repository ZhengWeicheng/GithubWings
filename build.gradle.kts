ext {
    set("compileSdkVersion", 35)
    set("buildToolsVersion", "29.0.0")
    set("minSdkVersion", 23)
    set("targetSdkVersion", 33)
}
buildscript {
    repositories {
        maven { url = uri("https://www.jitpack.io") }
        maven { url = uri("https://jitpack.io") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.7.0")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}