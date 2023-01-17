

plugins {
    kotlin("jvm")
    `maven-publish`
}
apply<pw.binom.plugins.BinomPublishPlugin>()

publishing {
    publications {
        create<MavenPublication>("Main") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
        }
    }
}
