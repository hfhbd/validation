import org.jetbrains.dokka.gradle.*

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
}

kotlin {
    setup()

    jvm()
    js(IR) {
        browser()
        nodejs()
    }

    // tier 1
    linuxX64()
    macosX64()
    macosArm64()
    iosSimulatorArm64()
    iosX64()

    // tier 2
    linuxArm64()
    watchosSimulatorArm64()
    watchosX64()
    watchosArm32()
    watchosArm64()
    tvosSimulatorArm64()
    tvosX64()
    tvosArm64()
    iosArm64()

    // tier 3
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX86()
    androidNativeX64()
    mingwX64()
    watchosDeviceArm64()
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
