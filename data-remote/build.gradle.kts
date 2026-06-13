plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))

    with(libs) {
        implementation(kotlin.stdlib)
        implementation(kotlin.coroutines.core)

        implementation(google.gson)
        implementation(retrofit)
    }
}
