// Standalone settings for the build-logic included build. It hosts the quality
// convention plugins (quickstart.detekt / quickstart.spotless / quickstart.jacoco)
// applied by the root project.
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "build-logic"
