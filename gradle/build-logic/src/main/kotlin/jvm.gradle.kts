plugins {
    kotlin("jvm")
    id("setup")
    id("io.github.hfhbd.serviceloader")
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
