plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.plugins.kotlin.jvm.toDep())
    implementation(libs.plugins.serviceloader.toDep())
    implementation(libs.plugins.mavencentral.toDep())
    implementation(libs.plugins.dokka.toDep())
    implementation(libs.plugins.kotlin.compiler.testing.toDep())
}

fun Provider<PluginDependency>.toDep() = map {
    "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}
