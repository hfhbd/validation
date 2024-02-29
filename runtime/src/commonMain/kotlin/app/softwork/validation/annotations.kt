package app.softwork.validation

import kotlinx.serialization.*

@SerialInfo
@Target(AnnotationTarget.PROPERTY)
public annotation class MinLength(val value: Int)

@SerialInfo
@Target(AnnotationTarget.PROPERTY)
public annotation class MaxLength(val value: Int)
