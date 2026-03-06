// Creditos: Leandro - Soberano - Versão Estável 2026
// APK Leve - Apenas Interface Multiconexão Firebase
plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.cybersoberano.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.cybersoberano.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 4
        versionName = "4.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true // Ativado para diminuir ainda mais o APK
            isShrinkResources = true // Remove recursos não usados
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")

    // ESTAS SÃO AS QUE DERAM ERRO - Versões atualizadas:
    implementation("org.jsoup:jsoup:1.18.1")
    implementation("com.google.guava:guava:33.2.1-android")

    // Imagens
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
