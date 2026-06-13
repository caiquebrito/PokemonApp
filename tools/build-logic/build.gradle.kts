plugins {
    `kotlin-dsl`
}

// The convention plugins use the typed DSLs of these plugins, so their Gradle
// plugin artifacts must be on this project's compile classpath. They share the
// main build's classloader at runtime, which is what lets detekt see the Kotlin
// plugin's extensions. Keep versions in sync with gradle/libs.versions.toml:
//   agp = "9.0.1", kotlin = "2.0.21", detekt = "1.23.7", spotless = "6.25.0".
dependencies {
    implementation("com.android.tools.build:gradle:9.0.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.21")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.7")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.25.0")
}
