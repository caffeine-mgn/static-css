import pw.binom.getGitBranch

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
