import pw.binom.Versions.KOTLIN_VERSION
import pw.binom.plugins.PublishInfo

plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish").version("0.20.0")
    id("pw.binom.publish")
    id("com.github.gmazzo.buildconfig")
}

buildConfig {
    packageName("pw.binom.css")
    buildConfigField("String", "KOTLIN_VERSION", "\"$KOTLIN_VERSION\"")
    buildConfigField("String", "STATIC_CSS_VERSION", "\"${project.version}\"")
}

//java.sourceSets["main"].java {
//    srcDir(project.buildDir.resolve("gen"))
//}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20")
    api("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.20")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20")
    api(gradleApi())
}
/*
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
*/
gradlePlugin {
    plugins {
        create("static-css") {
            id = "pw.binom.static-css"
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

//apply<pw.binom.plugins.DocsPlugin>()
