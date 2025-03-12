package app.softwork.validation.plugin.kotlin.fir

import app.softwork.validation.plugin.kotlin.fir.ValidationErrors.VALIDATION_MAXLENGTH_NOT_STRING
import app.softwork.validation.plugin.kotlin.fir.ValidationErrors.VALIDATION_MINLENGTH_NOT_STRING
import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.error0
import org.jetbrains.kotlin.diagnostics.rendering.BaseDiagnosticRendererFactory
import org.jetbrains.kotlin.diagnostics.rendering.RootDiagnosticRendererFactory
import org.jetbrains.kotlin.psi.KtElement

internal object ValidationErrors {
    val VALIDATION_MAXLENGTH_NOT_STRING by error0<KtElement>()
    val VALIDATION_MINLENGTH_NOT_STRING by error0<KtElement>()

    init {
        RootDiagnosticRendererFactory.registerFactory(ValidationErrorMessages)
    }
}

internal data object ValidationErrorMessages : BaseDiagnosticRendererFactory() {
    override val MAP: KtDiagnosticFactoryToRendererMap = KtDiagnosticFactoryToRendererMap("Validation").apply {
        put(
            VALIDATION_MAXLENGTH_NOT_STRING,
            "MaxLength requires type kotlin.String",
        )
        put(
            VALIDATION_MINLENGTH_NOT_STRING,
            "MinLength requires type kotlin.String",
        )
    }
}
