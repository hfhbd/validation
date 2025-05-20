import app.softwork.validation.MinLength

class A(
    @MinLength(2)
    val s: String,
)

fun box() = "OK"
