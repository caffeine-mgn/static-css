import pw.binom.publish.getExternalVersion

plugins {
    id("pw.binom.publish") version "0.1.18" apply false
    id("com.github.gmazzo.buildconfig") version "3.0.3" apply false
}

allprojects {
    group = "pw.binom.static-css"
    version = getExternalVersion()

    repositories {
        mavenLocal()
        mavenCentral()
        maven(url = "https://repo.binom.pw/releases")
    }
}
