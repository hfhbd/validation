package app.softwork.validation.plugin.kotlin.ir

import org.jetbrains.kotlin.backend.common.extensions.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.visitors.*

internal data object ValidationInitExtensionRegistrar : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        moduleFragment.transformChildrenVoid(ValidationTransformer(pluginContext))
    }
}
