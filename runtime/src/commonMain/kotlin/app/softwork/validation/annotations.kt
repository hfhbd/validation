package app.softwork.validation

import kotlinx.serialization.*

@SerialInfo
@Target(AnnotationTarget.PROPERTY)
public annotation class MinLength(val inclusive: Int)

@SerialInfo
@Target(AnnotationTarget.PROPERTY)
public annotation class MaxLength(val inclusive: Int)
