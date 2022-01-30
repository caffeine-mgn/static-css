

plugins {
    kotlin("jvm")
    `maven-publish`
}
apply<pw.binom.plugins.BinomPublishPlugin>()

publishing {
    publications {
        create<MavenPublication>("BinomRepository") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
        }
    }
}
