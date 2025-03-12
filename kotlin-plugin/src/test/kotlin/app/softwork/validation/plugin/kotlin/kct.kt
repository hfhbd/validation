package app.softwork.validation.plugin.kotlin

import app.softwork.validation.plugin.kotlin.fir.ValidationFirExtensionRegistrar
import app.softwork.validation.plugin.kotlin.ir.ValidationInitExtensionRegistrar
import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.jetbrains.kotlin.backend.common.extensions.*
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter

fun jvmCompile(vararg files: SourceFile, validate: (JvmCompilationResult) -> Unit): List<String> {
    val dumps = mutableListOf<String>()
    val result = KotlinCompilation()
        .apply {
            sources = files.toList()
            compilerPluginRegistrars = listOf(ValidationCompilerPluginRegistrarTest(dumps::add))
            inheritClassPath = true
        }
        .compile()
    validate(result)
    return dumps
}

private class ValidationCompilerPluginRegistrarTest(private val dump: (String) -> Unit) : CompilerPluginRegistrar() {
    override val supportsK2 = true
    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        FirExtensionRegistrarAdapter.registerExtension(ValidationFirExtensionRegistrar)
        IrGenerationExtension.registerExtension(ValidationInitExtensionRegistrar(dump))
    }
}
