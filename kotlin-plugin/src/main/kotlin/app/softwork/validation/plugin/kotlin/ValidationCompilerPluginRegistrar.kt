package app.softwork.validation.plugin.kotlin

import app.softwork.serviceloader.*
import org.jetbrains.kotlin.backend.common.extensions.*
import org.jetbrains.kotlin.compiler.plugin.*
import org.jetbrains.kotlin.config.*

@ServiceLoader(CompilerPluginRegistrar::class)
public class ValidationCompilerPluginRegistrar : CompilerPluginRegistrar() {
    override val supportsK2: Boolean = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        IrGenerationExtension.registerExtension(ValidationInitExtensionRegistrar(null))
    }
}
