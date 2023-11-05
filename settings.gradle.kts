rootProject.name = "static-css"

include(":generator")
include(":plugin")
pluginManagement {
    repositories {
        mavenCentral()
        maven(url = "https://repo.binom.pw")
        gradlePluginPortal()
    }
}
