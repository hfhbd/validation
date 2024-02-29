import app.softwork.validation.*

fun main(vararg args: String) {
    println(args[0])
    println(args[1])
    A(
        a = args[0],
        b = args[1],
    )
}

class A(
    @property:MinLength(2)
    @property:MaxLength(4)
    val a: String,

    @property:MinLength(2)
    @property:MaxLength(4)
    val b: String,
) {
    init {
        error("Should not happen")
    }
}
