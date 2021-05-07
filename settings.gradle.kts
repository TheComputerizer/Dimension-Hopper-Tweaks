pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            name = "Minecraft Forge Maven"
            url = uri("https://maven.minecraftforge.net")
        }
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "net.minecraftforge.gradle") {
                useModule("${requested.id}:ForgeGradle:${requested.version}")
            }
        }
    }
}

rootProject.name = "Dimension-Hopper-Tweaks"
