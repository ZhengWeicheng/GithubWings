plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.wings.githubwings"
    compileSdk = rootProject.extra["compileSdkVersion"] as Int

    defaultConfig {
        applicationId = "com.wings.githubwings"
        minSdk = rootProject.extra["minSdkVersion"] as Int
        targetSdk = rootProject.extra["targetSdkVersion"] as Int
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    signingConfigs {
        create("release") {
            storeFile = file("./wings.jks")
            storePassword = "1qaz2wsx"
            keyAlias = "wings"
            keyPassword = "1qaz2wsx"
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            buildConfigField("String", "httpBaseUrl", "\"https://api.github.com/\"")
            buildConfigField("String", "clientId", "\"Iv23liP7xuOG2XFosMHy\"")
            buildConfigField(
                "String",
                "clientSecret",
                "\"18078bb158399ffc662ad4b5493ce62301a227f6\""
            )
            buildConfigField("String", "authBaseUrl", "\"https://github.com/\"")
            buildConfigField("String", "redirectUrl", "\"github://oauth\"")
        }
        getByName("release") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "clientId", "\"Iv23liP7xuOG2XFosMHy\"")
            buildConfigField(
                "String",
                "clientSecret",
                "\"18078bb158399ffc662ad4b5493ce62301a227f6\""
            )
            buildConfigField("String", "httpBaseUrl", "\"https://api.github.com/\"")
            buildConfigField("String", "authBaseUrl", "\"https://github.com/\"")
            buildConfigField("String", "redirectUrl", "\"github://oauth\"")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.accompanist.systemuicontroller)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    implementation(libs.mmkv.static)
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    api(libs.retrofit2.retrofit)
    api(libs.logging.interceptor)
    api(libs.converter.gson)
    api(libs.kotlin.reflect)
    api(libs.kotlinx.coroutines.android)
    implementation(libs.com.google.code.gson.gson7)
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("org.mockito:mockito-core:4.0.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("org.mockito:mockito-inline:4.6.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    implementation(libs.coil.compose)
    implementation(libs.androidx.material)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}