import org.gradle.api.publish.maven.*

plugins {
    kotlin("multiplatform")
    id("setup")
}

kotlin {
    setup()

    jvm()

    js {
        browser()
        nodejs()
    }
    wasmJs {
        nodejs()
    }
    wasmWasi {
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

val emptyJar by tasks.registering(Jar::class)

publishing {
    publications.withType(MavenPublication::class).configureEach {
        artifact(emptyJar) {
            classifier = "javadoc"
        }
    }
}
