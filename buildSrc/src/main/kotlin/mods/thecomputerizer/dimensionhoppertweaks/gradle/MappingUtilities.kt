@file:JvmName("MappingUtilities")

package mods.thecomputerizer.dimensionhoppertweaks.gradle

import net.minecraftforge.gradle.common.util.HashFunction
import net.minecraftforge.gradle.common.util.MavenArtifactDownloader
import net.minecraftforge.gradle.common.util.Utils
import net.minecraftforge.gradle.mcp.util.MCPWrapper
import org.gradle.api.Project
import java.io.File
import java.nio.file.Path

internal fun findMcpConfig(project: Project, version: String?): File? {
    return MavenArtifactDownloader.manual(project, "de.oceanlabs.mcp:mcp_config:${version}@zip", false)
}

internal fun wrap(project: Project, data: File?, version: String?): MCPWrapper = MCPWrapper(HashFunction.SHA1.hash(data), data, cache(project, version).toFile())

internal fun cache(project: Project, version: String?): Path {
    return Utils.getCache(project, "mcp_repo")
        .toPath()
        .resolve("de")
        .resolve("oceanlabs")
        .resolve("mcp")
        .resolve("mcp_config")
        .resolve(version ?: ".")
}

