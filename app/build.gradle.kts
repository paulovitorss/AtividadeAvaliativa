plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.symbol.processing)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "br.com.unemat.paulo.atividadeavaliativa"
    compileSdk = 35

    defaultConfig {
        applicationId = "br.com.unemat.paulo.atividadeavaliativa"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Dependências de UI e Core
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.androidx.cardview)

    // Lifecycle
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging.interceptor)

    // Persistência e Reatividade
    implementation(libs.datastore.preferences.rxjava3)
    implementation(libs.rxjava3.java)
    implementation(libs.rxjava3.android)

    // Segurança e JWT
    implementation(libs.security.crypto)
    implementation(libs.auth0.jwtdecode)
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // Hilt (usando KSP)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Testes
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}