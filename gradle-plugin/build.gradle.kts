plugins {
    `kotlin-dsl`
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
    implementation(libs.plugins.kotlin.jvm.toDep())
}

fun Provider<PluginDependency>.toDep() = map {
    "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}

configurations.runtimeElements {
    attributes {
        attribute(
            GradlePluginApiVersion.GRADLE_PLUGIN_API_VERSION_ATTRIBUTE,
            objects.named<GradlePluginApiVersion>(GradleVersion.version("9.0.0").version)
        )
    }
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

tasks.pluginUnderTestMetadata {
    pluginClasspath.from(configurations.runtimeClasspath)
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
