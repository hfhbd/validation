import io.github.hfhbd.validation.MinLength

class A(
    @MinLength(2)
    val s: String,
)

fun box() = "OK"
