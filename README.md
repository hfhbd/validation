# Validation

Compiler plugin to validate the length of String properties

```kotlin
import app.softwork.validation.MinLength
import app.softwork.validation.MaxLength

class A(
    @MinLength(inclusive = 2)
    @MaxLength(inclusive = 4)
    val a: String,
) {
    init {
        error("Expected!")
    }
}

fun main() {
    A(a = "a") // IllegalArgumentException: a.length >= 2, was a
    A(a = "abcde") // IllegalArgumentException: a.length <= 4, was abcde
    A(a = "abc") // IllegalStateException: Expected!
}
```
