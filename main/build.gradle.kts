plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.ctb.main"
    compileSdk =
        libs.versions.compileSdk
            .get()
            .toInt()

    defaultConfig {
        minSdk =
            libs.versions.minSdk
                .get()
                .toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.valueOf(libs.versions.javaVersion.get())
        targetCompatibility = JavaVersion.valueOf(libs.versions.javaVersion.get())
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":data-remote"))
    implementation(project(":common"))
    implementation(project(":commonKotlin"))
    implementation(project(":presentation"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    with(libs.okhttp) {
        implementation(okhttp)
        implementation(loggingInterceptor)
    }

    with(libs.koin) {
        implementation(android)
        implementation(test)
        implementation(junit)
    }

    with(libs) {
        implementation(androidx.lifecycle.viewmodel.ktx)
        testImplementation(mockK)
        testImplementation(kotlin.coroutines.test)
    }
}
