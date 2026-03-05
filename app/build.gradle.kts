plugins {
    alias(libs.plugins.android.application)
    // Ativador do Google Services para o Firebase funcionar [cite: 2026-03-02]
    id("com.google.gms.google-services")
}

android {
    namespace = "com.cybersoberano.app"
    compileSdk = 35 // Ajustado para versão estável compatível com as libs atuais [cite: 2026-03-02]

    defaultConfig {
        applicationId = "com.cybersoberano.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 3
        versionName = "3.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // --- FERRAMENTAS DO SISTEMA DE GRUPOS (Soberano) --- [cite: 2026-03-02]

    // Gerenciador de versões do Firebase [cite: 2026-03-02]
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))

    // Banco de Dados em tempo real e Analytics [cite: 2026-03-02]
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-analytics")

    // Jsoup: Para capturar nome e foto direto do link do WhatsApp [cite: 2026-03-02]
    implementation("org.jsoup:jsoup:1.15.4")

    // Glide: Para carregar as imagens dos grupos com alta performance [cite: 2026-03-02]
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
}

// Créditos: dev Leandro - CyberSoberano [cite: 2026-01-31]