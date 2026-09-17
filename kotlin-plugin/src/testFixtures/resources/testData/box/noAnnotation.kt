import io.github.hfhbd.validation.MinLength
import io.github.hfhbd.validation.MaxLength
import io.github.hfhbd.validation.ValidationException

class A(
    val s: String,
    val d: String?,
) {
    init {
        if (s.length < 2) {
            throw ValidationException("s.length >= 2, was " + s)
        }
        if (s.length > 4) {
            throw ValidationException("s.length <= 4, was " + s)
        }
        if (d != null && d.length < 2) {
            throw ValidationException("d.length >= 2, was " + d)
        }
        if (d != null && d.length > 4) {
            throw ValidationException("d.length <= 4, was " + d)
        }
    }
}

fun box() = "OK"
