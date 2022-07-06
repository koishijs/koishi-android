rootProject.name = "Koishi"

include(":app")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            // Gradle plugins
            // Android Gradle Plugin (Defined AGP version)
            library("android-gradle", "com.android.tools.build", "gradle").version("7.2.1")
            // Kotlin Gradle Plugin (Defined Kotlin language version)
            library("kotlin-gradle", "org.jetbrains.kotlin", "kotlin-gradle-plugin").version("1.6.21")

            // Android desugar
            library("android-desugar", "com.android.tools", "desugar_jdk_libs").version("1.1.5")

            // Jetpack
            library("androidx-appcompat", "androidx.appcompat", "appcompat").version("1.4.2")
            library("androidx-lifecycle", "androidx.lifecycle", "lifecycle-runtime-ktx").version("2.3.1")
            library("androidx-preference", "androidx.preference", "preference-ktx").version("1.2.0")

            // Jetpack Compose
            library("androidx-compose-activity", "androidx.activity", "activity-compose").version("1.4.0")
            library("androidx-compose-material", "androidx.compose.material", "material").version("1.1.1")
            library("androidx-compose-animation", "androidx.compose.animation", "animation").version("1.1.1")
            library("androidx-compose-ui", "androidx.compose.ui", "ui-tooling").version("1.1.1")
            library("androidx-compose-lifecycle", "androidx.lifecycle", "lifecycle-viewmodel-compose").version("2.4.1")
            library("androidx-compose-navigation", "androidx.navigation", "navigation-compose").version("2.5.0")

            // Material Design
            library("google-android-material", "com.google.android.material", "material").version("1.4.0")
        }
    }
}
