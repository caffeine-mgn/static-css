import pw.binom.Versions.KOTLIN_VERSION

plugins {
    kotlin("jvm")
    `java-gradle-plugin`
}

apply<pw.binom.plugins.BinomPublishPlugin>()

java.sourceSets["main"].java {
    srcDir(project.buildDir.resolve("gen"))
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.0")
    api("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.6.0")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.0")
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
    plugins {
        create("static-css") {
            id = "static-css"
            implementationClass = "pw.binom.css.CssPlugin"
            description = "Binom Static Css Library"
        }
    }
}
apply<pw.binom.plugins.DocsPlugin>()
