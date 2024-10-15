plugins {
    kotlin("jvm")
    id("setup")
    id("app.softwork.serviceloader-compiler")
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
