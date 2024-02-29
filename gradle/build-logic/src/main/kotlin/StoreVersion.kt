import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.*
import java.io.*

@CacheableTask
abstract class StoreVersion : DefaultTask() {
    @get:Input
    abstract val version: Property<String>

    init {
        version.convention(project.version.toString())
    }

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    init {
        outputDirectory.convention(project.layout.buildDirectory.dir("generated/validation"))
    }

    @TaskAction
    fun action() {
        val outputDirectory = outputDirectory.asFile.get()
        val packageDir = File(outputDirectory, "app/softwork/validation/plugin/gradle")
        packageDir.mkdirs()
        File(packageDir, "Version.kt").writeText(
            """
            |package app.softwork.validation.plugin.gradle
            |
            |internal val VERSION: String = "${version.get()}"
            |
            """.trimMargin(),
        )
    }
}
