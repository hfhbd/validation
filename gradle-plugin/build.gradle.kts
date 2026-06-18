plugins {
    id("java-gradle-plugin")
    kotlin("jvm")
    id("setup")
    id("java-test-fixtures")
}

kotlin {
    setup()
}

java {
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    compileOnly(libs.plugins.kotlin.jvm.toDep())
}

fun Provider<PluginDependency>.toDep() = map {
    dependencyFactory.create("${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}")
}

tasks.validatePlugins {
    enableStricterValidation.set(true)
}

val storeVersion by tasks.registering(StoreVersion::class)
sourceSets.main {
    kotlin.srcDir(storeVersion)
}

gradlePlugin.plugins.register("validation") {
    id = "app.softwork.validation"
    implementationClass = "app.softwork.validation.plugin.gradle.ValidationPlugin"
    displayName = "Validation Gradle Plugin"
    description = "Validation Gradle Plugin"
}

val s = configurations.resolvable("s") {
    dependencies.addLater(libs.plugins.kotlin.serialization.toDep())
    dependencies.addLater(libs.plugins.kotlin.jvm.toDep())
}

tasks.pluginUnderTestMetadata {
    pluginClasspath.from(s)
}

testing.suites.named("test", JvmTestSuite::class) {
    useKotlinTest()

    targets.configureEach {
        testTask {
            environment("fixtureDir", project.file("src/testFixtures").path)

            javaLauncher.set(javaToolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(21))
            })
        }
    }
}
