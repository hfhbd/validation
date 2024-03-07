package app.softwork.validation

@Target(AnnotationTarget.PROPERTY)
public annotation class MinLength(val inclusive: Int)

@Target(AnnotationTarget.PROPERTY)
public annotation class MaxLength(val inclusive: Int)
