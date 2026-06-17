import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

private fun loadKeystoreProperties(): Properties {
    val props = Properties()
    val file = rootProject.file("keystore.properties")
    if (file.exists()) props.load(file.inputStream())
    return props
}

android {
    namespace = "com.ctb.pokemon"
    compileSdk =
        libs.versions.compileSdk
            .get()
            .toInt()

    defaultConfig {
        applicationId = "com.ctb.pokemon"
        minSdk =
            libs.versions.minSdk
                .get()
                .toInt()
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "DEFAULT_API_URL", "\"https://pokeapi.co/api/v2/\"")
    }

    signingConfigs {
        create("release") {
            val keystore = loadKeystoreProperties()
            storeFile = file(keystore["storeFile"] as? String ?: System.getenv("KEYSTORE_PATH") ?: "release.jks")
            storePassword = keystore["storePassword"] as? String ?: System.getenv("KEYSTORE_PASSWORD").orEmpty()
            keyAlias = keystore["keyAlias"] as? String ?: System.getenv("KEY_ALIAS").orEmpty()
            keyPassword = keystore["keyPassword"] as? String ?: System.getenv("KEY_PASSWORD").orEmpty()
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
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
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    implementation(project(":main"))
    implementation(project(":common"))
    implementation(project(":presentation"))

    implementation(libs.koin.android)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.core.splashscreen)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
