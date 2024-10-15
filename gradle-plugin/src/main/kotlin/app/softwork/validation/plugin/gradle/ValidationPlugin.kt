package app.softwork.validation.plugin.gradle

import org.gradle.api.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.SourceSet.*
import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.gradle.plugin.*
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet.Companion.COMMON_MAIN_SOURCE_SET_NAME

public class ValidationPlugin : KotlinCompilerPluginSupportPlugin {
    override fun apply(target: Project) {
        super.apply(target)

        target.plugins.withId("org.jetbrains.kotlin.multiplatform") {
            val kotlin = target.extensions.getByType(KotlinMultiplatformExtension::class.java)
            kotlin.sourceSets.configureEach {
                dependencies {
                    implementation(runtimeDependency())
                }
            }
        }
        target.plugins.withId("org.jetbrains.kotlin.jvm") {
            val kotlin = target.extensions.getByType(KotlinJvmProjectExtension::class.java)
            kotlin.sourceSets.configureEach {
                dependencies {
                    implementation(runtimeDependency())
                }
            }
        }
    }

    private fun runtimeDependency() = "app.softwork.validation:runtime:$VERSION"

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

    override fun getCompilerPluginId(): String = "app.softwork.validation"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = "app.softwork.validation",
        artifactId = "kotlin-plugin",
        version = VERSION,
    )

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> =
        kotlinCompilation.project.providers.provider { emptyList() }
}
