import pw.binom.Versions.KOTLIN_VERSION
import pw.binom.plugins.PublishInfo

plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish").version("0.20.0")
}

apply<pw.binom.plugins.BinomPublishPlugin>()

java.sourceSets["main"].java {
    srcDir(project.buildDir.resolve("gen"))
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    api("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.6.10")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    api(gradleApi())
}

tasks {
    val generateVersion = create("generateVersion") {
        val sourceDir = project.buildDir.resolve("gen/pw/binom")
        sourceDir.mkdirs()
        val versionSource = sourceDir.resolve("version.kt")
        outputs.files(versionSource)
        inputs.property("version", project.version)

        versionSource.writeText(
            """package pw.binom
            
const val STATIC_CSS_VERSION = "${project.version}"
const val KOTLIN_VERSION = "$KOTLIN_VERSION"
"""
        )
    }
    val classes by getting {
        dependsOn(generateVersion)
    }
}

gradlePlugin {
    this.isAutomatedPublishing = false
    plugins {
        create("static-css") {
            id = "static-css"
            displayName = "Static-Css Plugin"
            implementationClass = "pw.binom.css.CssPlugin"
            description = "Binom Static Css Library"
        }
    }
}

pluginBundle {
    website = PublishInfo.HTTP_PATH_TO_PROJECT
    vcsUrl = PublishInfo.GIT_PATH_TO_PROJECT
    tags = listOf("kotlin", "css")
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

apply<pw.binom.plugins.DocsPlugin>()
