package app.softwork.validation

public class ValidationException(override val message: String): IllegalArgumentException(message)
