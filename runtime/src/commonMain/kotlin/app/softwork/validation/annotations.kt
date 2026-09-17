package io.github.hfhbd.validation

@MustBeDocumented
@Target(AnnotationTarget.PROPERTY)
public annotation class MinLength(val inclusive: Int)

@MustBeDocumented
@Target(AnnotationTarget.PROPERTY)
public annotation class MaxLength(val inclusive: Int)
