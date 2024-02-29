package app.softwork.validation.plugin.kotlin

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.KotlinCompilation.ExitCode
import com.tschuchort.compiletesting.SourceFile
import org.jetbrains.kotlin.backend.common.extensions.*
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import kotlin.test.assertEquals

fun jvmCompile(vararg files: SourceFile): List<String> {
    val dumps = mutableListOf<String>()
    val result = KotlinCompilation()
        .apply {
            sources = files.toList()
            compilerPluginRegistrars = listOf(ValidationCompilerPluginRegistrarTest(dumps::add))
            inheritClassPath = true
            supportsK2 = true
            languageVersion = "2.0"
        }
        .compile()
    assertEquals(ExitCode.OK, result.exitCode)
    return dumps
}

private class ValidationCompilerPluginRegistrarTest(private val dump: (String) -> Unit) : CompilerPluginRegistrar() {
    override val supportsK2 = true
    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        IrGenerationExtension.registerExtension(ValidationInitExtensionRegistrar(dump))
    }
}
