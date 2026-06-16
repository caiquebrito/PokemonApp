// Standalone settings for the build-logic included build. It hosts the quality
// convention plugins (pokemon.detekt / pokemon.spotless / pokemon.jacoco)
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
