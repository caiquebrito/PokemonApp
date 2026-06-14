// detekt — static analysis (./gradlew detekt)
// Convention plugin applied to the root project; configures every module.
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

// Read the JVM target from the version catalog the consuming build defines.
val javaTarget = the<org.gradle.api.artifacts.VersionCatalogsExtension>()
    .named("libs")
    .findVersion("javaTarget").get().requiredVersion

subprojects {
    apply<io.gitlab.arturbosch.detekt.DetektPlugin>()

    extensions.configure<DetektExtension> {
        buildUponDefaultConfig = true
        parallel = true
        config.setFrom(rootProject.files("tools/detekt/detekt.yml"))
        baseline = rootProject.file("tools/detekt/baseline.xml")
    }
    tasks.withType<Detekt>().configureEach {
        jvmTarget = javaTarget
        reports {
            html.required.set(true)
            xml.required.set(true)
            sarif.required.set(false)
            txt.required.set(false)
        }
    }
    tasks.withType<DetektCreateBaselineTask>().configureEach {
        jvmTarget = javaTarget
    }
}
