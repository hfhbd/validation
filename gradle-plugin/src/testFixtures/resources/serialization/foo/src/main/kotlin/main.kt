import app.softwork.validation.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

fun main(vararg args: String) {
    Json.decodeFromString(A.serializer(), "{\"a\":\"${args[0]}\"}")
}

@Serializable
class A(
    @MinLength(2)
    @MaxLength(4)
    val a: String,
)
