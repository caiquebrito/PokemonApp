// spotless — ktlint formatting (./gradlew spotlessCheck / spotlessApply)
// Convention plugin applied to the root project; configures every module.
import com.diffplug.gradle.spotless.SpotlessExtension

subprojects {
    apply<com.diffplug.gradle.spotless.SpotlessPlugin>()

    extensions.configure<SpotlessExtension> {
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
}
