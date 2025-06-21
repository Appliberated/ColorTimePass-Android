plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.appgramming.colortimepass"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.appgramming.colortimepass"
        minSdk = 14
        targetSdk = 35
        versionCode = 3
        versionName = "1.1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
}