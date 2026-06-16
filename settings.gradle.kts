pluginManagement {
    // Quality convention plugins (pokemon.detekt / spotless / jacoco).
    includeBuild("tools/build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "PokemonApp"
include(":app")
include(":data")
include(":data-remote")
include(":domain")
include(":presentation")
include(":main")
include(":design")
include(":common")
include(":commonKotlin")
