plugins {
    kotlin("jvm")
    id("setup")
    id("app.softwork.serviceloader")
    id("com.google.devtools.ksp")
}

kotlin {
    setup()
}

publishing {
    publications.register<MavenPublication>("mavenJava") {
        from(components["java"])
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}
