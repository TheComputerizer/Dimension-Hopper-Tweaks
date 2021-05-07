package mods.thecomputerizer.dimensionhoppertweaks.gradle

import net.minecraftforge.gradle.common.util.MavenArtifactDownloader
import net.minecraftforge.gradle.mcp.MCPRepo
import net.minecraftforge.gradle.userdev.UserDevExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class FindMappingsZip : DefaultTask() {
    var file: File? = null

    @TaskAction
    fun apply() {
        val minecraft = project.extensions.getByType(UserDevExtension::class.java)
        this.file = MavenArtifactDownloader.generate(project, MCPRepo.getMappingDep(minecraft.mappingChannel, minecraft.mappingVersion), false)
    }
}
