plugins {
    id("publish")
    id("org.jetbrains.dokka")
}

dokka {
    val module = project.name
    dokkaSourceSets.configureEach {
        reportUndocumented = true
        includes.from("README.md")
        val sourceSetName = name
        File("$module/src/$sourceSetName").takeIf { it.exists() }?.let {
            sourceLink {
                localDirectory = file("src/$sourceSetName/kotlin")
                remoteUrl = uri("https://github.com/hfhbd/validation/tree/main/$module/src/$sourceSetName/kotlin")
                remoteLineSuffix = "#L"
            }
        }
    }
}
