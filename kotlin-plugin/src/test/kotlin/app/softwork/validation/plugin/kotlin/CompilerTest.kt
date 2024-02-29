package app.softwork.validation.plugin.kotlin

import com.tschuchort.compiletesting.SourceFile
import kotlin.test.Test
import kotlin.test.assertEquals

class CompilerTest {
	@Test
	fun success() {
		val anno = SourceFile.kotlin(
			"anno.kt",
			"""package app.softwork.validation

@Target(AnnotationTarget.PROPERTY)
annotation class MinLength(val size: Int)

@Target(AnnotationTarget.PROPERTY)
annotation class MaxLength(val size: Int)
			""",
		)

		val source = SourceFile.kotlin(
			"main.kt",
			"""import app.softwork.validation.MinLength
import app.softwork.validation.MaxLength
import kotlin.IllegalArgumentException

class A(
    @property:MinLength(2)
    @property:MaxLength(4)
val s: String,
) {
    init {
	    if (s.length < 2) {
            throw IllegalArgumentException("s.length >= 2, was " + s)
        }
        if (s.length > 4) {
            throw IllegalArgumentException("s.length <= 4, was " + s)
        }
    }
}
			""",
		)
		val blocks = jvmCompile(anno, source)
		assertEquals(
"""WHEN type=kotlin.Unit origin=IF
  BRANCH
    if: CALL 'public final fun less (arg0: kotlin.Int, arg1: kotlin.Int): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=null
      arg0: CALL 'public open fun <get-length> (): kotlin.Int declared in kotlin.String' type=kotlin.Int origin=null
        ${'$'}this: CALL 'public final fun <get-s> (): kotlin.String declared in <root>.A' type=kotlin.String origin=null
          ${'$'}this: GET_VAR '<this>: <root>.A declared in <root>.A' type=<root>.A origin=null
      arg1: CONST Int type=kotlin.Int value=2
    then: CALL 'public final fun illegalArgumentException (arg0: kotlin.String): kotlin.Nothing declared in kotlin.internal.ir' type=kotlin.Nothing origin=null
      arg0: STRING_CONCATENATION type=kotlin.String
        CONST String type=kotlin.String value="s.length >= "
        CONST Int type=kotlin.Int value=2
        CONST String type=kotlin.String value=", was "
        CALL 'public final fun <get-s> (): kotlin.String declared in <root>.A' type=kotlin.String origin=null
          ${'$'}this: GET_VAR '<this>: <root>.A declared in <root>.A' type=<root>.A origin=null
""", blocks[0])
		assertEquals(
"""WHEN type=kotlin.Unit origin=IF
  BRANCH
    if: CALL 'public final fun greater (arg0: kotlin.Int, arg1: kotlin.Int): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=null
      arg0: CALL 'public open fun <get-length> (): kotlin.Int declared in kotlin.String' type=kotlin.Int origin=null
        ${'$'}this: CALL 'public final fun <get-s> (): kotlin.String declared in <root>.A' type=kotlin.String origin=null
          ${'$'}this: GET_VAR '<this>: <root>.A declared in <root>.A' type=<root>.A origin=null
      arg1: CONST Int type=kotlin.Int value=4
    then: CALL 'public final fun illegalArgumentException (arg0: kotlin.String): kotlin.Nothing declared in kotlin.internal.ir' type=kotlin.Nothing origin=null
      arg0: STRING_CONCATENATION type=kotlin.String
        CONST String type=kotlin.String value="s.length <= "
        CONST Int type=kotlin.Int value=4
        CONST String type=kotlin.String value=", was "
        CALL 'public final fun <get-s> (): kotlin.String declared in <root>.A' type=kotlin.String origin=null
          ${'$'}this: GET_VAR '<this>: <root>.A declared in <root>.A' type=<root>.A origin=null
""", blocks[1])

	}
}
