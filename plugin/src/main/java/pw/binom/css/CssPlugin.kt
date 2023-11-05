package pw.binom.css

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.DependencyResolutionListener
import org.gradle.api.artifacts.ResolvableDependencies
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.process.CommandLineArgumentProvider
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import pw.binom.KOTLIN_VERSION
import pw.binom.STATIC_CSS_VERSION

open class CssPlugin : Plugin<Project> {
    fun findRuntimeClasspath(target: Project) =
        when {
            "runtime" in target.configurations.names -> target.configurations.getAt("runtime")
            "runtimeClasspath" in target.configurations.names -> target.configurations.getAt("runtimeClasspath")
            else -> throw GradleException("Can't find runtime configuration")
        }

    override fun apply(target: Project) {
        target.pluginManager.apply("org.jetbrains.kotlin.jvm")
        target.gradle.addListener(object : DependencyResolutionListener {
            override fun beforeResolve(dependencies: ResolvableDependencies) {
                val config = findRuntimeClasspath(target)
                config.dependencies.add(target.dependencies.create("org.jetbrains.kotlin:kotlin-stdlib:$KOTLIN_VERSION"))
                config.dependencies.add(target.dependencies.create("pw.binom.static-css:generator:$STATIC_CSS_VERSION"))
                target.dependencies.add(
                    "api",
                    target.dependencies.create("pw.binom.static-css:generator:$STATIC_CSS_VERSION")
                )
                target.gradle.removeListener(this)
            }

            override fun afterResolve(dependencies: ResolvableDependencies) {
            }
        })
        val generateMainTask = target.tasks.register("generateCssMainSource", GenerateMain::class.java)
        val compileKotlin = target.tasks.findByName("compileKotlin") as KotlinCompile
        compileKotlin.dependsOn(generateMainTask.get())
        val mainFileDir = target.buildDir.resolve("css-gen-main")
        generateMainTask.get().mainFile.set(mainFileDir.resolve("GeneratedMain.kt"))
        val kotlin = target.extensions.getByName("kotlin") as KotlinJvmProjectExtension
        kotlin.sourceSets.findByName("main")!!.kotlin.srcDir(mainFileDir)
        val generateCss = target.tasks.register("buildCss", GenerateCss::class.java)
        generateCss.get().dependsOn(compileKotlin)
        generateCss.get().outputCss.set(target.buildDir.resolve("css/${target.name}.css"))
        val runtimeClasspath = findRuntimeClasspath(target)
        generateCss.get().classpath = compileKotlin.outputs.files + runtimeClasspath
    }
}

abstract class GenerateCss : JavaExec() {

    @get:OutputFile
    abstract val outputCss: RegularFileProperty

    init {
        group = "build"
        mainClass.set("pw.binom.css.GeneratedMain")
        argumentProviders += CommandLineArgumentProvider { arrayListOf(outputCss.asFile.get().absolutePath) }
        outputCss.set(project.objects.fileProperty())
    }

    override fun exec() {
        classpath += project.configurations.getByName("compileClasspath")
        super.exec()
    }
}

abstract class GenerateMain : DefaultTask() {
    @OutputFile
    val mainFile = project.objects.fileProperty()

    init {
        group = "build"
    }

    @TaskAction
    fun execute() {
        mainFile.asFile.get().parentFile.mkdirs()
        mainFile.asFile.get().outputStream().bufferedWriter().use {
            it.append("package pw.binom.css\n\n")
                .append("import java.io.File\n\n")
                .append("object GeneratedMain{\n")
                .append("\t@JvmStatic\n")
                .append("\tfun main(args:Array<String>){\n")
                .append("\t\tFile(args[0]).outputStream().bufferedWriter().use {\n")
                .append("\t\t\tstyle.buildRecursive(it)\n")
                .append("\t\t}\n")
                .append("\t}\n")
                .append("}")
        }
    }
}
