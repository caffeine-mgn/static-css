/*
buildscript {

    repositories {
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.23")
    }
}
*/
plugins {
    kotlin("jvm") version "1.9.23"
    id("com.github.gmazzo.buildconfig") version "3.0.3"
}
val kotlinVersion = kotlin.coreLibrariesVersion
buildConfig {
    packageName("pw.binom.css")
    buildConfigField("String", "KOTLIN_VERSION", "\"$kotlinVersion\"")
}

repositories {
    mavenLocal()
    mavenCentral()
//    maven(url = "https://repo.binom.pw")
    maven(url = "https://plugins.gradle.org/m2/")
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
}
