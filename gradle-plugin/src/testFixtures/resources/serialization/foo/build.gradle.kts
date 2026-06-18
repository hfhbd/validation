plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("application")
    id("app.softwork.validation")
}

application.mainClass.set("MainKt")

dependencies {
    implementation(libs.serialization.json)
}
