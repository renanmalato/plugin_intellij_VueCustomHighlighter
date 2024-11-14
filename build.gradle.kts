plugins {
    id("org.jetbrains.intellij") version "1.17.2"
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

intellij {
    version.set("2024.1")
    type.set("IU")
    plugins.set(listOf(
        "JavaScript",
        "org.jetbrains.plugins.vue"
    ))
}

tasks {
    buildSearchableOptions {
        enabled = false
    }
    
    patchPluginXml {
        sinceBuild.set("241")
        untilBuild.set("243.*")
        pluginDescription.set("""
            Highlights Vue components in different colors.
            - Custom components in #23A27F
            - Boolean/null literals in #FF9E64
        """.trimIndent())
        changeNotes.set("Initial release")
    }
}