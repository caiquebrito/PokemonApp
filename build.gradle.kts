// Top-level build file where you can add configuration options common to all sub-projects/modules.
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.spotless) apply false
    jacoco // provides jacocoClasspath for the root-level aggregated report
}

// ---------------------------------------------------------------------------
// Code quality: applied uniformly to every module.
//   detekt   — static analysis        (./gradlew detekt)
//   spotless — ktlint formatting       (./gradlew spotlessCheck / spotlessApply)
//   jacoco   — unit-test coverage      (./gradlew jacocoTestReport)
// ---------------------------------------------------------------------------
subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "com.diffplug.spotless")
    apply(plugin = "jacoco")

    // ---- detekt -----------------------------------------------------------
    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        buildUponDefaultConfig = true
        parallel = true
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
        baseline = rootProject.file("config/detekt/baseline.xml")
    }
    tasks.withType<Detekt>().configureEach {
        jvmTarget = libs.versions.javaTarget.get()
        reports {
            html.required.set(true)
            xml.required.set(true)
            sarif.required.set(false)
            txt.required.set(false)
        }
    }
    tasks.withType<DetektCreateBaselineTask>().configureEach {
        jvmTarget = libs.versions.javaTarget.get()
    }

    // ---- spotless (ktlint) ------------------------------------------------
    extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("src/**/*.kt")
            targetExclude("**/build/**")
            ktlint().editorConfigOverride(
                mapOf(
                    // Compose @Composable functions use PascalCase by design.
                    "ktlint_standard_function-naming" to "disabled",
                    // ':data-remote' maps to package com.ctb.data_remote (modules can't use '-').
                    "ktlint_standard_package-name" to "disabled",
                ),
            )
        }
        kotlinGradle {
            target("*.gradle.kts")
            ktlint()
        }
    }

    // ---- jacoco -----------------------------------------------------------
    // Enable coverage instrumentation for Android unit tests on the debug type.
    plugins.withId("com.android.library") {
        extensions.configure<com.android.build.api.dsl.LibraryExtension> {
            buildTypes.getByName("debug") { enableUnitTestCoverage = true }
        }
    }
    plugins.withId("com.android.application") {
        extensions.configure<com.android.build.api.dsl.ApplicationExtension> {
            buildTypes.getByName("debug") { enableUnitTestCoverage = true }
        }
    }
}

// Per-module jacoco report, wired once each module's plugins are known.
// Handles both pure-JVM modules and Android library/application modules.
subprojects {
    afterEvaluate {
        val isAndroid = plugins.hasPlugin("com.android.library") ||
            plugins.hasPlugin("com.android.application")
        // Depend on the test task by name (lazily): Android registers its unit-test
        // tasks in its own afterEvaluate, which may run after this block.
        val testTaskName = if (isAndroid) "testDebugUnitTest" else "test"

        val coverageExcludes = listOf(
            "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*",
            "**/*Test*.*", "**/di/**", "**/*_Factory*.*",
            // Compose/UI scaffolding that isn't unit-tested.
            "**/*ComposableSingletons*.*", "**/*\$*Preview*.*",
        )
        val kotlinClasses = fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
            exclude(coverageExcludes)
        }
        val javaClasses = fileTree(layout.buildDirectory.dir("classes/kotlin/main")) {
            exclude(coverageExcludes)
        }
        val sourceDirs = files("src/main/java", "src/main/kotlin")

        // The `jacoco` plugin already registers `jacocoTestReport` for JVM modules;
        // Android modules have no such task. Configure the existing one or create it.
        val configure: JacocoReport.() -> Unit = {
            group = "verification"
            description = "Generates a Jacoco coverage report for unit tests."
            dependsOn(testTaskName)
            classDirectories.setFrom(if (isAndroid) kotlinClasses else javaClasses)
            sourceDirectories.setFrom(sourceDirs)
            executionData.setFrom(
                fileTree(layout.buildDirectory) {
                    include(
                        "jacoco/test.exec",
                        "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                    )
                },
            )
            reports {
                html.required.set(true)
                xml.required.set(true)
            }
        }
        if (tasks.findByName("jacocoTestReport") != null) {
            tasks.named<JacocoReport>("jacocoTestReport", configure)
        } else {
            tasks.register<JacocoReport>("jacocoTestReport", configure)
        }
    }
}

// Aggregated, repo-wide coverage report across every module.
tasks.register<JacocoReport>("jacocoAggregatedReport") {
    group = "verification"
    description = "Aggregates Jacoco unit-test coverage across all modules."
    dependsOn(subprojects.map { "${it.path}:jacocoTestReport" })
    val reports = subprojects.map { it.tasks.named<JacocoReport>("jacocoTestReport") }
    classDirectories.setFrom(reports.map { it.map { r -> r.classDirectories } })
    sourceDirectories.setFrom(reports.map { it.map { r -> r.sourceDirectories } })
    executionData.setFrom(reports.map { it.map { r -> r.executionData } })
    reports {
        html.required.set(true)
        xml.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/aggregated/html"))
        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/aggregated/jacoco.xml"))
    }
}
