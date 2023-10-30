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
            library("androidx-preference", "androidx.preference", "preference-ktx").version("1.2.0")
        }
    }
}
