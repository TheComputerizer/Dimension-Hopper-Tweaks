import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.20"
}

repositories {
    mavenCentral()
    maven {
        name = "Forge Maven"
        url = uri("https://maven.minecraftforge.net")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(group = "net.minecraftforge.gradle", name = "ForgeGradle", version = "4.1.10")
    implementation(group = "net.minecraftforge", name = "artifactural", version = "2.0.3")
    implementation(group = "net.minecraftforge", name = "srgutils", version = "0.4.1")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}
