pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Calendar"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":features:widget")
include(":core")
include(":baselineprofile")
