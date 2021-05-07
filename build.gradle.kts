import net.minecraftforge.gradle.common.util.RunConfig
import org.gradle.kotlin.dsl.NamedDomainObjectContainerScope

import java.time.format.DateTimeFormatter
import java.time.Instant

plugins {
    java
    eclipse
    idea
    `maven-publish`
    id("net.minecraftforge.gradle") version "4.1.10"
}

version = "1.0"
group = "mods.thecomputerizer.dimensionhoppertweaks"

minecraft {
    mappings("stable", "39-1.12")
    // accessTransformer = file('src/main/resources/META-INF/accesstransformer.cfg')
    runs {
        createWithDefaults("client")
        createWithDefaults("server") { args("nogui") }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    minecraft(group = "net.minecraftforge", name = "forge", version = "1.12.2-14.23.5.2855")
}

tasks.withType<Jar> {
    archiveBaseName.set("dimension-hopper-tweaks")
    finalizedBy("reobfJar")

    manifest {
        attributes(
            "Specification-Title" to project.name,
            "Specification-Version" to project.version,
            "Specification-Vendor" to "TheComputerizer",
            "Implementation-Title" to "${project.group}.${project.name.toLowerCase().replace(' ', '_')}",
            "Implementation-Version" to project.version,
            "Implementation-Vendor" to "TheComputerizer",
            "Implementation-Timestamp" to DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        )
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
}

tasks.withType<Wrapper> {
    gradleVersion = "6.8.3"
    distributionType = Wrapper.DistributionType.ALL
}

fun NamedDomainObjectContainerScope<RunConfig>.createWithDefaults(name: String, config: RunConfig.() -> Unit = {}) {
    if (name.isBlank()) return
    this.create(name) {
        workingDirectory(file("run_${name}"))

        property("forge.logging.console.level", "debug")

        this.apply(config)
    }
}
