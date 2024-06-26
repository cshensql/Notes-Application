import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.6.20"
    kotlin("plugin.serialization") version "1.6.10"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.26.0"
}

group = "net.codebot"
version = "4.0.0"

val compileKotlin: KotlinCompile by tasks
val compileJava: JavaCompile by tasks
compileJava.destinationDirectory.set(compileKotlin.destinationDirectory)

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":shared"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation ("org.jsoup:jsoup:1.15.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("org.controlsfx:controlsfx:11.1.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainModule.set("application")
    mainClass.set("net.codebot.application.Main")
}

javafx {
    // version is determined by the plugin above
    version = "18.0.2"
    modules = listOf("javafx.controls", "javafx.graphics", "javafx.web")
}

// https://stackoverflow.com/questions/74453018/jlink-package-kotlin-in-both-merged-module-and-kotlin-stdlib
jlink {
    forceMerge("kotlin")
    launcher {
        name = "Notes"
    }
    jpackage {
        // Since windows and mac needs different icon format, we need to check
        if (org.gradle.internal.os.OperatingSystem.current().isMacOsX) {
            icon = "src/main/resources/notes.icns"
        } else if (org.gradle.internal.os.OperatingSystem.current().isWindows) {
            icon = "src/main/resources/notes.ico"
        } // Otherwise, we will use the default one

    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}