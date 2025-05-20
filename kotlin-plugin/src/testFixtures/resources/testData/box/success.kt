import app.softwork.validation.MinLength
import app.softwork.validation.MaxLength
import app.softwork.validation.ValidationException

class A(
    @MinLength(2) @property:MaxLength(4) val s: String,
    @MinLength(2) @property:MaxLength(4) val d: String?,
)

fun box() = "OK"
