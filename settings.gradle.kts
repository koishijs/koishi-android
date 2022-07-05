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
            library("kotlin-gradle", "org.jetbrains.kotlin", "kotlin-gradle-plugin").version("1.6.10")

            // Android desugar
            library("android-desugar", "com.android.tools", "desugar_jdk_libs").version("1.1.5")

            // Deprecated Android Support
            library("android-support-appcompat", "com.android.support", "appcompat-v7").version("28.0.0")
            library("android-support-preference", "com.android.support", "preference-v7").version("28.0.0")

            // Test
            library("junit", "junit", "junit").version("4.13.2")
            library("android-test-runner", "com.android.support.test", "runner").version("1.0.2")
            library("android-test-espresso", "com.android.support.test.espresso", "espresso-core").version("3.0.2")
        }
    }
}
