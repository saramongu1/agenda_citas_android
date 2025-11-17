plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // AGREGAR ESTE PLUGIN (sin version aquí)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.agenda_optica_isis"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.agenda_optica_isis"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    packaging {
        resources {
            excludes += setOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/INDEX.LIST",
                "META-INF/NOTICE.md",
                "META-INF/LICENSE.md",
                "META-INF/io.netty.versions.properties"
            )
        }
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // TUS DEPENDENCIAS ACTUALES
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.activity)
    implementation(libs.swiperefreshlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Otras dependencias
    implementation("com.google.android.material:material:1.9.0")
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation("androidx.cardview:cardview:1.0.0")

    // JavaMail
    implementation("com.sun.mail:android-mail:1.6.7")
    implementation("com.sun.mail:android-activation:1.6.7")

    // FIREBASE - SEGÚN DOCUMENTACIÓN ACTUAL
    implementation(platform("com.google.firebase:firebase-bom:32.7.0")) // Puedes usar 34.5.0 si prefieres
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-analytics")

    // Utilidades para Firebase
    implementation("com.google.code.gson:gson:2.10.1")

    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}