plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "cn.anillc.koishi"
        minSdk = 24
        targetSdk = 28
        versionCode = 1
        versionName = "0.0.1"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true
        dataBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        resources {
            excludes += "META-INF/*"
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0-beta03"
    }
}

dependencies {

    coreLibraryDesugaring(libs.android.desugar)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.preference)

    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.lifecycle)
    implementation(libs.androidx.compose.navigation)
}
