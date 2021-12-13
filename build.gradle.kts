buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.0")
    }
}


allprojects {
    group = "pw.binom.static-css"
    version = "0.1.30"

    repositories {
        mavenLocal()
        mavenCentral()
        maven(url = "https://repo.binom.pw/releases")
    }
}