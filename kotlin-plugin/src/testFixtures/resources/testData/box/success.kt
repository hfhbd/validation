import io.github.hfhbd.validation.MinLength
import io.github.hfhbd.validation.MaxLength
import io.github.hfhbd.validation.ValidationException

class A(
    @MinLength(2) @property:MaxLength(4) val s: String,
    @MinLength(2) @property:MaxLength(4) val d: String?,
)

fun box() = "OK"
