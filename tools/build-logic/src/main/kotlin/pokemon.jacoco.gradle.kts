// jacoco — unit-test coverage (./gradlew jacocoTestReport / jacocoAggregatedReport)
// Convention plugin applied to the root project; configures every module and the
// repo-wide aggregated report.
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension

// The root needs the jacoco plugin too, so the aggregated report task can resolve
// the jacoco ant classpath.
pluginManager.apply("jacoco")

subprojects {
    apply(plugin = "jacoco")

    // Enable coverage instrumentation for Android unit tests on the debug type.
    plugins.withId("com.android.library") {
        extensions.configure<LibraryExtension> {
            buildTypes.getByName("debug") { enableUnitTestCoverage = true }
        }
    }
    plugins.withId("com.android.application") {
        extensions.configure<ApplicationExtension> {
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
            // Visual / Compose UI — verified via Robolectric correctness tests, not line coverage
            // (Robolectric's classloader prevents on-the-fly JaCoCo from attributing those lines).
            // Global rule, applied to every module — follows the project's UI naming convention:
            //   *Screen / *Content / *Route composables, Activities, and the design `compose` pkg.
            "**/*Screen*.*", "**/*Content*.*", "**/*Route*.*",
            "**/*Activity*.*",
            "**/compose/**",
        )
        // Android Kotlin class output moved across AGP versions; include the known locations
        // (each fileTree rooted at its own dir so package paths stay correct).
        val androidClassDirs =
            listOf(
                "tmp/kotlin-classes/debug",
                "intermediates/built_in_kotlinc/debug/compileDebugKotlin/classes",
            ).map { path ->
                fileTree(layout.buildDirectory.dir(path)) { exclude(coverageExcludes) }
            }
        val kotlinClasses = files(androidClassDirs)
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
