import mods.thecomputerizer.dimensionhoppertweaks.gradle.ExtractMappingsZip
import mods.thecomputerizer.dimensionhoppertweaks.gradle.FindMappingsZip
import mods.thecomputerizer.dimensionhoppertweaks.gradle.GenerateObfToSrg
import net.minecraftforge.gradle.common.util.RunConfig
import net.minecraftforge.gradle.userdev.DependencyManagementExtension
import org.gradle.kotlin.dsl.NamedDomainObjectContainerScope

import java.time.format.DateTimeFormatter
import java.time.Instant

plugins {
    java
    eclipse
    idea
    `maven-publish`
    id("net.minecraftforge.gradle")
}

version = "1.0"
group = "mods.thecomputerizer.dimensionhoppertweaks"

minecraft {
    mappings("stable", "39-1.12")

    accessTransformer(file("dep_at.cfg")) // Apply dependency ATs

    runs {
        createWithDefaults("client")
        createWithDefaults("server") { args("nogui") }
    }
}

repositories {
    mavenCentral()
    maven {
        name = "Progwml6"
        url = uri("https://dvs1.progwml6.com/files/maven/")
    }
    maven {
        name = "ModMaven"
        url = uri("https://modmaven.k-4u.nl")
    }
    maven {
        name = "CurseForge Repository"
        url = uri("https://www.cursemaven.com/")

        content {
            includeGroup("curse.maven")
        }
    }
}

dependencies {
    minecraft(group = "net.minecraftforge", name = "forge", version = "1.12.2-14.23.5.2855")

    implementation(fg.deobf(curseMaven(mod = "fermion-lib", pid = 345538L, fid = 3186519L)))
    implementation(fg.deobf(curseMaven(mod = "codechicken-lib", pid = 242818L, fid = 2779848L)))
    implementation(fg.deobf(curseMaven(mod = "avaritia", pid = 261348L, fid = 3143349L)))

    //compileOnly(fg.deobf(group = "mezz.jei", name = "jei_1.12.2", version = "4.16.1.302", classifier = "api"))
    runtimeOnly(fg.deobf(group = "mezz.jei", name = "jei_1.12.2", version = "4.16.1.302"))
}

sourceSets.main {
    resources.outputDir = java.outputDir
}

tasks {
    val findMappingsZip = create("findMappingsZip", FindMappingsZip::class)

    val createObfToSrg = create("createObfToSrg", GenerateObfToSrg::class) {
        version = "1.12.2"
        output = file("build/$name/obfToSrg.srg")

        dependsOn(findMappingsZip)
    }

    val extractMappingsZip = create("extractMappingsZip", ExtractMappingsZip::class) {
        inputFile = { findMappingsZip.file }
        outputDirectory = file("build/$name")

        dependsOn(findMappingsZip)
    }

    minecraft.runs.forEach {
        it.property("net.minecraftforge.gradle.GradleStart.srg.notch-srg", createObfToSrg.output)
        it.property("net.minecraftforge.gradle.GradleStart.csvDir", extractMappingsZip.outputDirectory)
    }

    whenTaskAdded {
        if (name.startsWith("prepareRun") && name != "prepareRun") { // Target prepareRunXxx
            dependsOn(createObfToSrg)
            dependsOn(extractMappingsZip)
        }
    }

    withType<Jar> {
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

    withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
    }

    withType<Wrapper> {
        gradleVersion = "6.8.3"
        distributionType = Wrapper.DistributionType.ALL
    }
}

fun NamedDomainObjectContainerScope<RunConfig>.createWithDefaults(name: String, config: RunConfig.() -> Unit = {}) {
    if (name.isBlank()) return
    this.create(name) {
        workingDirectory(file("run_${name}"))

        property("forge.logging.console.level", "debug")
        property("fml.coreMods.load", "net.thesilkminer.mc.fermion.asm.common.FermionPlugin")

        this.apply(config)
    }
}

fun curseMaven(mod: String, pid: Long, fid: Long): ExternalModuleDependency {
    return project.dependencies.create(group = "curse.maven", name = "$mod-$pid", version = fid.toString())
}

fun DependencyManagementExtension.deobf(group: String, name: String, version: String? = null,
                                        configuration: String? = null, classifier: String? = null,
                                        ext: String? = null): Dependency {
    return this.deobf(project.dependencies.create(group, name, version, configuration, classifier, ext))
}
