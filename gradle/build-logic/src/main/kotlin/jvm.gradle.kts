import org.jetbrains.dokka.gradle.*

plugins {
    kotlin("jvm")
    id("setup")
    id("app.softwork.serviceloader")
    id("com.google.devtools.ksp")
    id("org.jetbrains.dokka")
}

kotlin {
    setup()
}

tasks.named<DokkaTaskPartial>("dokkaHtmlPartial") {
    dokkaSourceSets.configureEach {
        externalDocumentationLink("https://cashapp.github.io/sqldelight/2.0.0/2.x/")
        externalDocumentationLink(
            url = "https://kotlinlang.org/api/kotlinx-datetime/",
            packageListUrl = "https://kotlinlang.org/api/kotlinx-datetime/kotlinx-datetime/package-list",
        )
        externalDocumentationLink("https://uuid.softwork.app")
        externalDocumentationLink("https://kotlinlang.org/api/kotlinx.coroutines/")
    }
}
