package io.github.hfhbd.validation.plugin.gradle

import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

public class ValidationPlugin : KotlinCompilerPluginSupportPlugin {
    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

    override fun getCompilerPluginId(): String = "io.github.hfhbd.validation"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = "io.github.hfhbd.validation",
        artifactId = "kotlin-plugin",
        version = VERSION,
    )

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        kotlinCompilation.defaultSourceSet {
            dependencies {
                implementation("io.github.hfhbd.validation:runtime:$VERSION")
            }
        }

        kotlinCompilation.compileTaskProvider.configure {
            it.compilerOptions.freeCompilerArgs.add("-Xcompiler-plugin-order=${getCompilerPluginId()}>org.jetbrains.kotlinx.serialization")
        }

        return kotlinCompilation.project.objects.listProperty(SubpluginOption::class.java)
    }
}
