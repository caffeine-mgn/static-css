import pw.binom.getGitBranch

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    }
}

allprojects {
    group = "pw.binom.static-css"
    val branch = getGitBranch()
    version = if (branch == "main" || branch == "master")
        pw.binom.Versions.LIB_VERSION
    else
        "${pw.binom.Versions.LIB_VERSION}-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
        maven(url = "https://repo.binom.pw/releases")
    }
}
