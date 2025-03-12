package app.softwork.validation.plugin.kotlin.fir

import app.softwork.validation.plugin.kotlin.fir.ValidationPredicateMatchingService.Companion.validationPredicateMatchingService
import org.jetbrains.kotlin.build.deserializeFromPlainText
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirValueParameterChecker
import org.jetbrains.kotlin.fir.analysis.checkers.fullyExpandedClassId
import org.jetbrains.kotlin.fir.declarations.FirValueParameter
import org.jetbrains.kotlin.fir.types.coneTypeOrNull
import org.jetbrains.kotlin.utils.addToStdlib.constant

internal data object ValidationPropertyChecker : FirValueParameterChecker(
    mppKind = MppCheckerKind.Common,
) {
    override fun check(
        declaration: FirValueParameter,
        context: CheckerContext,
        reporter: DiagnosticReporter
    ) {
        if (context.session.validationPredicateMatchingService.isAnnotatedMaxLength(declaration.symbol)) {
            checkMaxLengthAnnotation(declaration, context, reporter)
        }
        if (context.session.validationPredicateMatchingService.isAnnotatedMinLength(declaration.symbol)) {
            checkMinLengthAnnotation(declaration, context, reporter)
        }
    }

    private fun checkMaxLengthAnnotation(
        declaration: FirValueParameter,
        context: CheckerContext,
        reporter: DiagnosticReporter
    ) {
        if (!declaration.isString(context.session)) {
            reporter.reportOn(
                declaration.returnTypeRef.source,
                ValidationErrors.VALIDATION_MAXLENGTH_NOT_STRING,
                context
            )
        }
    }
    private fun checkMinLengthAnnotation(
        declaration: FirValueParameter,
        context: CheckerContext,
        reporter: DiagnosticReporter
    ) {
        if (!declaration.isString(context.session)) {
            reporter.reportOn(
                declaration.returnTypeRef.source,
                ValidationErrors.VALIDATION_MINLENGTH_NOT_STRING,
                context
            )
        }
    }

    private fun FirValueParameter.isString(session: FirSession): Boolean {
        val classID = returnTypeRef.coneTypeOrNull?.fullyExpandedClassId(session) ?: return false
        return classID == session.builtinTypes.stringType.id
    }
}
