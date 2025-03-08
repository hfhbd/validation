import org.jetbrains.dokka.gradle.*

plugins {
    id("publish")
    id("org.jetbrains.dokka")
}

dokka {
    val module = project.name
    dokkaSourceSets.configureEach {
        reportUndocumented.set(true)
        includes.from("README.md")
        val sourceSetName = name
        File("$module/src/$sourceSetName").takeIf { it.exists() }?.let {
            sourceLink {
                localDirectory.set(file("src/$sourceSetName/kotlin"))
                remoteUrl.set(uri("https://github.com/hfhbd/validation/tree/main/$module/src/$sourceSetName/kotlin"))
                remoteLineSuffix.set("#L")
            }
        }
    }
}
