plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("application")
    id("io.github.hfhbd.validation")
}

application.mainClass.set("MainKt")

dependencies {
    implementation(libs.serialization.json)
}
