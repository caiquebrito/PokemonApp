// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false

    // ---------------------------------------------------------------------------
    // Code quality: each tool is a convention plugin in the tools/build-logic
    // included build, applied uniformly to every module.
    //   detekt   — static analysis    (./gradlew detekt)
    //   spotless — ktlint formatting   (./gradlew spotlessCheck / spotlessApply)
    //   jacoco   — unit-test coverage  (./gradlew jacocoTestReport / jacocoAggregatedReport)
    // ---------------------------------------------------------------------------
    id("pokemon.detekt")
    id("pokemon.spotless")
    id("pokemon.jacoco")
}
