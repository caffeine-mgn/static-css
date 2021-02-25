import java.net.URI

plugins {
    kotlin("jvm")
    `maven-publish`
    `java-gradle-plugin`
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib:1.4.30")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.30")
    api("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.4.30")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.30")
    api(gradleApi())
}

val BINOM_REPO_URL = "binom.repo.url"
val BINOM_REPO_USER = "binom.repo.user"
val BINOM_REPO_PASSWORD = "binom.repo.password"

if (hasProperty(BINOM_REPO_URL) && hasProperty(BINOM_REPO_USER) && hasProperty(BINOM_REPO_PASSWORD)) {
    publishing {
        repositories {
            maven {
                name = "BinomRepository"
                url = URI(property(BINOM_REPO_URL) as String)
                credentials {
                    username = property(BINOM_REPO_USER) as String
                    password = property(BINOM_REPO_PASSWORD) as String
                }
            }
        }
        publications {
            create<MavenPublication>("BinomRepository") {
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()
                from(components["java"])

                pom {
                    scm {
                        connection.set("https://github.com/caffeine-mgn/static-css.git")
                        url.set("https://github.com/caffeine-mgn/static-css")
                    }
                    developers {
                        developer {
                            id.set("subochev")
                            name.set("Anton Subochev")
                            email.set("caffeine.mgn@gmail.com")
                        }
                    }
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                }
            }
        }
    }
}

gradlePlugin {
    plugins {
        create("static-css") {
            id = "static-css"
            implementationClass = "pw.binom.css.CssPlugin"
            description = "Binom Static Css Library"
        }
    }
}