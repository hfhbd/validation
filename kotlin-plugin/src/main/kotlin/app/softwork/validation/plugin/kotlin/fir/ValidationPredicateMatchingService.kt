package app.softwork.validation.plugin.kotlin.fir

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.extensions.FirDeclarationPredicateRegistrar
import org.jetbrains.kotlin.fir.extensions.FirExtensionSessionComponent
import org.jetbrains.kotlin.fir.extensions.predicate.LookupPredicate
import org.jetbrains.kotlin.fir.extensions.predicateBasedProvider
import org.jetbrains.kotlin.fir.symbols.impl.FirValueParameterSymbol
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

internal class ValidationPredicateMatchingService(session: FirSession) : FirExtensionSessionComponent(session) {
    companion object {

        val minLengthFq = FqName("app.softwork.validation.MinLength")
        val minLengthClassId = ClassId.Companion.topLevel(minLengthFq)

        val maxLengthFq = FqName("app.softwork.validation.MaxLength")
        val maxLengthClassId = ClassId.Companion.topLevel(maxLengthFq)

        val minLengthPredicate = LookupPredicate.Companion.create {
            annotated(minLengthFq)
        }
        val maxLengthPredicate = LookupPredicate.Companion.create {
            annotated(maxLengthFq)
        }

        val FirSession.validationPredicateMatchingService: ValidationPredicateMatchingService by FirSession.sessionComponentAccessor()
    }

    override fun FirDeclarationPredicateRegistrar.registerPredicates() {
        register(minLengthPredicate, maxLengthPredicate)
    }

    fun isAnnotatedMinLength(symbol: FirValueParameterSymbol): Boolean {
        return session.predicateBasedProvider.matches(minLengthPredicate, symbol)
    }

    fun isAnnotatedMaxLength(symbol: FirValueParameterSymbol): Boolean {
        return session.predicateBasedProvider.matches(maxLengthPredicate, symbol)
    }
}
