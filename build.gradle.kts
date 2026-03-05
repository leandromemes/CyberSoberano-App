plugins {
    alias(libs.plugins.android.application) apply false
    // Adicione a linha abaixo:
    id("com.google.gms.google-services") version "4.4.2" apply false
}