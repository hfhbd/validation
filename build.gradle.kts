plugins {
    id("org.jetbrains.dokka")
}

dokka {
    dokkaPublications.configureEach {
        includes.from("README.md")
    }

    dependencies {
        for (sub in subprojects) {
            dokka(sub)
        }
    }
}
