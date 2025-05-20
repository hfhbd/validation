package app.softwork.validation.plugin.kotlin.fir

import org.jetbrains.kotlin.diagnostics.error0
import org.jetbrains.kotlin.diagnostics.rendering.RootDiagnosticRendererFactory
import org.jetbrains.kotlin.psi.KtElement

internal data object ValidationErrors {
    val VALIDATION_MAXLENGTH_NOT_STRING by error0<KtElement>()
    val VALIDATION_MINLENGTH_NOT_STRING by error0<KtElement>()

    init {
        RootDiagnosticRendererFactory.registerFactory(ValidationErrorMessages)
    }
}
