plugins {
    kotlin("jvm")
    id("application")
    id("app.softwork.validation")
    id("java-test-fixtures")
}

application.mainClass.set("MainKt")

configurations.testFixturesCompileClasspath {
    exclude("app.softwork.validation", "runtime")
}
