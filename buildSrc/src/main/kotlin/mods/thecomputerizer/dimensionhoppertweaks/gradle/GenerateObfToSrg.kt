package mods.thecomputerizer.dimensionhoppertweaks.gradle

import net.minecraftforge.srgutils.IMappingFile
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.ByteArrayInputStream
import java.io.File

open class GenerateObfToSrg : DefaultTask() {
    @get:Input
    var version: String? = null
    @get:OutputFile
    var output: File? = null

    @TaskAction
    fun apply() {
        val mcp = findMcpConfig(this.project, this.version)
        val wrapper = wrap(this.project, mcp, this.version)
        val data = wrapper.getData("mappings")
        val input = IMappingFile.load(ByteArrayInputStream(data))
        input.write(this.output?.toPath(), IMappingFile.Format.SRG, false)
    }
}
