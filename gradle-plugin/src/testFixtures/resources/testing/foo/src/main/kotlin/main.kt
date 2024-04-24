import app.softwork.validation.*

fun main(vararg args: String) {
    println(args[0])
    println(args.getOrNull(1))
    A(
        a = args[0],
        b = args.getOrNull(1),
    )
}

class A(
    @MinLength(2)
    @MaxLength(4)
    val a: String,

    @MinLength(2)
    @MaxLength(4)
    val b: String?,
) {
    init {
        error("Should not happen")
    }
}
