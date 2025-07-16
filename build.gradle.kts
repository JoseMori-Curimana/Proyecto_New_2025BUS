// build.gradle.kts en la raíz del proyecto
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.9.2")
        classpath("com.google.gms:google-services:4.4.0")
    }
}
