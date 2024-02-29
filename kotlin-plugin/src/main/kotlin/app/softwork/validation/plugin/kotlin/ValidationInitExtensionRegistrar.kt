package app.softwork.validation.plugin.kotlin

import org.jetbrains.kotlin.backend.common.extensions.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.visitors.*

internal class ValidationInitExtensionRegistrar(private val dump: ((String) -> Unit)?) : IrGenerationExtension {
	override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
		moduleFragment.transformChildrenVoid(ValidationTransformer(pluginContext, dump))
	}
}
