

plugins {
    kotlin("jvm")
    `maven-publish`
    id("pw.binom.publish")
}

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
