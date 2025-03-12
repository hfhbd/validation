package app.softwork.validation.plugin.kotlin

import com.tschuchort.compiletesting.*
import com.tschuchort.compiletesting.KotlinCompilation.ExitCode
import kotlin.test.*

class CompilerTest {
    @Test
    fun success() {
        val source = SourceFile.kotlin(
            "main.kt",
            """import app.softwork.validation.MinLength
import app.softwork.validation.MaxLength
import app.softwork.validation.ValidationException

class A(
    @MinLength(2)
    @property:MaxLength(4)
    val s: String,

    @MinLength(2)
    @property:MaxLength(4)
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
			""",
        )

        val blocks = jvmCompile(source) { result ->
            assertEquals(ExitCode.OK, result.exitCode)
        }
        assertEquals(4, blocks.size)
        assertEquals(
            """WHEN type=kotlin.Unit origin=IF
  BRANCH
    if: CALL 'public final fun less (arg0: kotlin.Int, arg1: kotlin.Int): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=LT
      arg0: CALL 'public open fun <get-length> (): kotlin.Int declared in kotlin.String' type=kotlin.Int origin=GET_PROPERTY
        ${'$'}this: CALL 'public final fun <get-s> (): kotlin.String declared in <root>.A' type=kotlin.String origin=GET_PROPERTY
          ${'$'}this: GET_VAR '<this>: <root>.A declared in <root>.A' type=<root>.A origin=null
      arg1: CONST Int type=kotlin.Int value=2
    then: THROW type=kotlin.Nothing
      CONSTRUCTOR_CALL 'public constructor <init> (message: kotlin.String) [primary] declared in app.softwork.validation.ValidationException' type=app.softwork.validation.ValidationException origin=null
        message: STRING_CONCATENATION type=kotlin.String
          CONST String type=kotlin.String value="s.length >= "
          CONST Int type=kotlin.Int value=2
          CONST String type=kotlin.String value=", was "
          CALL 'public final fun <get-s> (): kotlin.String declared in <root>.A' type=kotlin.String origin=GET_PROPERTY
            ${'$'}this: GET_VAR '<this>: <root>.A declared in <root>.A' type=<root>.A origin=null
""",
            blocks[0]
        )
        assertEquals(
            """WHEN type=kotlin.Unit origin=IF
  BRANCH
    if: CALL 'public final fun greater (arg0: kotlin.Int, arg1: kotlin.Int): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=GT
      arg0: CALL 'public open fun <get-length> (): kotlin.Int declared in kotlin.String' type=kotlin.Int origin=GET_PROPERTY
        ${'$'}this: CALL 'public final fun <get-s> (): kotlin.String declared in <root>.A' type=kotlin.String origin=GET_PROPERTY
          ${'$'}this: GET_VAR '<this>: <root>.A declared in <root>.A' type=<root>.A origin=null
      arg1: CONST Int type=kotlin.Int value=4
    then: THROW type=kotlin.Nothing
      CONSTRUCTOR_CALL 'public constructor <init> (message: kotlin.String) [primary] declared in app.softwork.validation.ValidationException' type=app.softwork.validation.ValidationException origin=null
        message: STRING_CONCATENATION type=kotlin.String
          CONST String type=kotlin.String value="s.length <= "
          CONST Int type=kotlin.Int value=4
          CONST String type=kotlin.String value=", was "
          CALL 'public final fun <get-s> (): kotlin.String declared in <root>.A' type=kotlin.String origin=GET_PROPERTY
            ${'$'}this: GET_VAR '<this>: <root>.A declared in <root>.A' type=<root>.A origin=null
""",
            blocks[1]
        )
        assertEquals(
            """WHEN type=kotlin.Unit origin=IF
  BRANCH
    if: WHEN type=kotlin.Boolean origin=ANDAND
      BRANCH
        if: CALL 'public final fun not (): kotlin.Boolean [operator] declared in kotlin.Boolean' type=kotlin.Boolean origin=EXCL
          ${'$'}this: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EQEQ
            arg0: CALL 'public final fun <get-d> (): kotlin.String? declared in <root>.A' type=kotlin.String? origin=GET_PROPERTY
              ${'$'}this: GET_VAR '<this>: <root>.A declared in <root>.A' type=<root>.A origin=null
            arg1: CONST Null type=kotlin.Nothing? value=null
        then: CALL 'public final fun less (arg0: kotlin.Int, arg1: kotlin.Int): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=LT
          arg0: CALL 'public open fun <get-length> (): kotlin.Int declared in kotlin.String' type=kotlin.Int origin=GET_PROPERTY
            ${'$'}this: CALL 'public final fun <get-d> (): kotlin.String? declared in <root>.A' type=kotlin.String? origin=GET_PROPERTY
              ${'$'}this: GET_VAR '<this>: <root>.A declared in <root>.A' type=<root>.A origin=null
          arg1: CONST Int type=kotlin.Int value=2
      BRANCH
        if: CONST Boolean type=kotlin.Boolean value=true
        then: CONST Boolean type=kotlin.Boolean value=false
    then: THROW type=kotlin.Nothing
      CONSTRUCTOR_CALL 'public constructor <init> (message: kotlin.String) [primary] declared in app.softwork.validation.ValidationException' type=app.softwork.validation.ValidationException origin=null
        message: STRING_CONCATENATION type=kotlin.String
          CONST String type=kotlin.String value="d.length >= "
          CONST Int type=kotlin.Int value=2
          CONST String type=kotlin.String value=", was "
          CALL 'public final fun <get-d> (): kotlin.String? declared in <root>.A' type=kotlin.String? origin=GET_PROPERTY
            ${'$'}this: GET_VAR '<this>: <root>.A declared in <root>.A' type=<root>.A origin=null
""",
            blocks[2]
        )
        assertEquals(
            """WHEN type=kotlin.Unit origin=IF
  BRANCH
    if: WHEN type=kotlin.Boolean origin=ANDAND
      BRANCH
        if: CALL 'public final fun not (): kotlin.Boolean [operator] declared in kotlin.Boolean' type=kotlin.Boolean origin=EXCL
          ${'$'}this: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EQEQ
            arg0: CALL 'public final fun <get-d> (): kotlin.String? declared in <root>.A' type=kotlin.String? origin=GET_PROPERTY
              ${'$'}this: GET_VAR '<this>: <root>.A declared in <root>.A' type=<root>.A origin=null
            arg1: CONST Null type=kotlin.Nothing? value=null
        then: CALL 'public final fun greater (arg0: kotlin.Int, arg1: kotlin.Int): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=GT
          arg0: CALL 'public open fun <get-length> (): kotlin.Int declared in kotlin.String' type=kotlin.Int origin=GET_PROPERTY
            ${'$'}this: CALL 'public final fun <get-d> (): kotlin.String? declared in <root>.A' type=kotlin.String? origin=GET_PROPERTY
              ${'$'}this: GET_VAR '<this>: <root>.A declared in <root>.A' type=<root>.A origin=null
          arg1: CONST Int type=kotlin.Int value=4
      BRANCH
        if: CONST Boolean type=kotlin.Boolean value=true
        then: CONST Boolean type=kotlin.Boolean value=false
    then: THROW type=kotlin.Nothing
      CONSTRUCTOR_CALL 'public constructor <init> (message: kotlin.String) [primary] declared in app.softwork.validation.ValidationException' type=app.softwork.validation.ValidationException origin=null
        message: STRING_CONCATENATION type=kotlin.String
          CONST String type=kotlin.String value="d.length <= "
          CONST Int type=kotlin.Int value=4
          CONST String type=kotlin.String value=", was "
          CALL 'public final fun <get-d> (): kotlin.String? declared in <root>.A' type=kotlin.String? origin=GET_PROPERTY
            ${'$'}this: GET_VAR '<this>: <root>.A declared in <root>.A' type=<root>.A origin=null
""",
            blocks[3]
        )
    }

    @Test
    fun serialName() {
        val source = SourceFile.kotlin(
            "main.kt",
            """import app.softwork.validation.MinLength
import kotlinx.serialization.SerialName

class A(
    @MinLength(2)
    @SerialName("foo")
    val s: String,
)
			""",
        )

        val blocks = jvmCompile(source) { result ->
            assertEquals(ExitCode.OK, result.exitCode)
        }
        assertEquals(1, blocks.size)
        assertEquals(
            """WHEN type=kotlin.Unit origin=IF
  BRANCH
    if: CALL 'public final fun less (arg0: kotlin.Int, arg1: kotlin.Int): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=LT
      arg0: CALL 'public open fun <get-length> (): kotlin.Int declared in kotlin.String' type=kotlin.Int origin=GET_PROPERTY
        ${'$'}this: CALL 'public final fun <get-s> (): kotlin.String declared in <root>.A' type=kotlin.String origin=GET_PROPERTY
          ${'$'}this: GET_VAR '<this>: <root>.A declared in <root>.A' type=<root>.A origin=null
      arg1: CONST Int type=kotlin.Int value=2
    then: THROW type=kotlin.Nothing
      CONSTRUCTOR_CALL 'public constructor <init> (message: kotlin.String) [primary] declared in app.softwork.validation.ValidationException' type=app.softwork.validation.ValidationException origin=null
        message: STRING_CONCATENATION type=kotlin.String
          CONST String type=kotlin.String value="foo.length >= "
          CONST Int type=kotlin.Int value=2
          CONST String type=kotlin.String value=", was "
          CALL 'public final fun <get-s> (): kotlin.String declared in <root>.A' type=kotlin.String origin=GET_PROPERTY
            ${'$'}this: GET_VAR '<this>: <root>.A declared in <root>.A' type=<root>.A origin=null
""",
            blocks[0]
        )
    }

    @Test
    fun wrongType() {
        val source = SourceFile.kotlin(
            "main.kt",
            """import app.softwork.validation.MinLength
import app.softwork.validation.MaxLength

data class A(
    @MinLength(2)
    @MaxLength(4)
    val s: Int,
)
			""",
        )

        val blocks = jvmCompile(source) { result ->
            assertEquals(ExitCode.COMPILATION_ERROR, result.exitCode)
            assertTrue("main.kt:7:12 MinLength requires type kotlin.String" in result.messages)
            assertTrue("main.kt:7:12 MaxLength requires type kotlin.String" in result.messages)
        }
        assertEquals(0, blocks.size)
    }
}
