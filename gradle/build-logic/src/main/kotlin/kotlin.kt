import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

fun KotlinProjectExtension.setup() {
    jvmToolchain(8)
    explicitApi()
}
