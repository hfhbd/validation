// RUN_PIPELINE_TILL: FRONTEND

import app.softwork.validation.MinLength
import app.softwork.validation.MaxLength

data class A(
    @MinLength(2)
    @MaxLength(4)
    val s: <!VALIDATION_MAXLENGTH_NOT_STRING, VALIDATION_MINLENGTH_NOT_STRING!>Int<!>,
)

/* GENERATED_FIR_TAGS: classDeclaration, data, integerLiteral, primaryConstructor, propertyDeclaration */
