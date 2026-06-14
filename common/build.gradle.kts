plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.ctb.common"
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
        buildConfigField("String", "DOMAIN_URL", "\"onrender.com\"")
        buildConfigField("String", "SHA256", "\"sha256/IX2/a47sFHkF9jewioc5OzEDzS0dNQjNMCX8PCQ26Pg=\"")
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

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    with(libs) {
        with(androidx) {
            with(lifecycle) {
                implementation(viewmodel.ktx)
                implementation(runtime.compose)
                implementation(runtime.ktx)
                implementation(process)
                implementation(viewmodel.compose)
                implementation(viewmodel.ktx)
                implementation(common)
            }
        }
        implementation(retrofit)
        implementation(retrofit.gsonConverter)
        implementation(retrofit.adapterRxjava2)
        implementation(okhttp.okhttp)
        implementation(okhttp.loggingInterceptor)
    }
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.compose.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    with(libs) {
        with(koin) {
            implementation(android)
            implementation(test)
            implementation(junit)
        }
    }
}
