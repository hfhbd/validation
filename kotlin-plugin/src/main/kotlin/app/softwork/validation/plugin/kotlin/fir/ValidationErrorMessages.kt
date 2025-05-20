package app.softwork.validation.plugin.kotlin.fir

import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.rendering.BaseDiagnosticRendererFactory

internal data object ValidationErrorMessages : BaseDiagnosticRendererFactory() {
    override val MAP: KtDiagnosticFactoryToRendererMap = KtDiagnosticFactoryToRendererMap("Validation").apply {
        put(
            ValidationErrors.VALIDATION_MAXLENGTH_NOT_STRING,
            "MaxLength requires type kotlin.String",
        )
        put(
            ValidationErrors.VALIDATION_MINLENGTH_NOT_STRING,
            "MinLength requires type kotlin.String",
        )
    }
}
