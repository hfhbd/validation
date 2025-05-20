package app.softwork.validation.plugin.kotlin.fir

import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

internal data object ValidationFirExtensionRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::ValidationPredicateMatchingService
        +::ValidationCheckers
    }
}
