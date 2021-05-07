package mods.thecomputerizer.dimensionhoppertweaks.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.zip.ZipFile

open class ExtractMappingsZip : DefaultTask() {
    @get:Input
    var inputFile: () -> File? = { null }

    @get:OutputDirectory
    var outputDirectory: File? = null

    @TaskAction
    fun apply() {
        (this.outputDirectory ?: return).let {
            it.deleteRecursively()
            it.mkdirs()
        }
        ZipFile(this.inputFile() ?: return).use { inputZip ->
            inputZip.entries().asSequence().forEach {
                val outPath = (this.outputDirectory ?: return).toPath().resolve(it.name).toAbsolutePath()
                inputZip.getInputStream(it).use { stream ->
                    Files.copy(stream, outPath, StandardCopyOption.REPLACE_EXISTING)
                }
            }
        }
    }
}
