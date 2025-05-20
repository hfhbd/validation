package app.softwork.validation.plugin.gradle

import org.gradle.api.*
import org.gradle.api.provider.*
import org.gradle.kotlin.dsl.listProperty
import org.jetbrains.kotlin.gradle.plugin.*

public class ValidationPlugin : KotlinCompilerPluginSupportPlugin {
    override fun apply(target: Project) {}

    private fun runtimeDependency() = "app.softwork.validation:runtime:$VERSION"

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

    override fun getCompilerPluginId(): String = "app.softwork.validation"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = "app.softwork.validation",
        artifactId = "kotlin-plugin",
        version = VERSION,
    )

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        kotlinCompilation.dependencies {
            implementation(runtimeDependency())
        }

        return kotlinCompilation.project.objects.listProperty()
    }
}
