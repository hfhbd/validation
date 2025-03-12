package app.softwork.validation.plugin.kotlin.fir

import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

internal object ValidationFirExtensionRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::ValidationPredicateMatchingService
        +::ValidationCheckers
    }
}
