import pw.binom.publish.getExternalVersion

plugins {
    id("pw.binom.publish") version "0.1.12" apply false
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
