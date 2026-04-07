package app.softwork.validation.plugin.gradle

import org.gradle.api.provider.*
import org.jetbrains.kotlin.gradle.plugin.*

public class ValidationPlugin : KotlinCompilerPluginSupportPlugin {
    private fun runtimeDependency() = "app.softwork.validation:runtime:$VERSION"

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

    override fun getCompilerPluginId(): String = "app.softwork.validation"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = "app.softwork.validation",
        artifactId = "kotlin-plugin",
        version = VERSION,
    )

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        kotlinCompilation.defaultSourceSet {
            dependencies {
                implementation(runtimeDependency())
            }
        }

        return kotlinCompilation.project.objects.listProperty(SubpluginOption::class.java)
    }
}
