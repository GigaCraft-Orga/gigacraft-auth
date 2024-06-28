plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.grafjojo"
version = "1.0"

repositories {
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.triumphteam.dev/snapshots/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
    implementation("dev.grafjojo:gigacraft-core:1.0")
    implementation("dev.triumphteam:triumph-cmd-bukkit:2.0.0-SNAPSHOT")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    assemble {
        dependsOn("shadowJar")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}