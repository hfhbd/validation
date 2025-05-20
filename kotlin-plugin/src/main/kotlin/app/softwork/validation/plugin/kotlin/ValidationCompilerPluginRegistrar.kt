package app.softwork.validation.plugin.kotlin

import app.softwork.serviceloader.*
import app.softwork.validation.plugin.kotlin.fir.ValidationFirExtensionRegistrar
import app.softwork.validation.plugin.kotlin.ir.ValidationInitExtensionRegistrar
import org.jetbrains.kotlin.backend.common.extensions.*
import org.jetbrains.kotlin.compiler.plugin.*
import org.jetbrains.kotlin.config.*
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter

@ServiceLoader(CompilerPluginRegistrar::class)
public class ValidationCompilerPluginRegistrar : CompilerPluginRegistrar() {
    override val supportsK2: Boolean = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        validation()
    }

    internal companion object {
        internal fun ExtensionStorage.validation() {
            FirExtensionRegistrarAdapter.registerExtension(ValidationFirExtensionRegistrar)
            IrGenerationExtension.registerExtension(ValidationInitExtensionRegistrar)
        }
    }
}
