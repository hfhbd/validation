package app.softwork.validation.plugin.gradle

import org.gradle.testkit.runner.*
import java.nio.file.*
import kotlin.io.path.*
import kotlin.test.*

@ExperimentalPathApi
class IntegrationTest {
    private val fixtureDir = Path(System.getenv("fixtureDir"))

    @Test
    fun success() {
        val projectDir = fixtureDir / "resources" / "testing"
        val buildResult = build(projectDir, ":foo:run", """--args=123 abcd""")
        val result = buildResult.task(":foo:run")!!

        assertEquals(
            TaskOutcome.FAILED,
            result.outcome,
        )
        assertTrue("""java.lang.IllegalStateException: Should not happen""" in buildResult.output)
    }

    @Test
    fun aTooShort() {
        val projectDir = fixtureDir / "resources" / "testing"
        val buildResult = build(projectDir, ":foo:run", """--args=1 abcd""")
        val result = buildResult.task(":foo:run")!!

        assertEquals(
            TaskOutcome.FAILED,
            result.outcome,
        )
        assertTrue("""java.lang.IllegalArgumentException: a.length >= 2, was 1""" in buildResult.output)
    }

    @Test
    fun aTooLong() {
        val projectDir = fixtureDir / "resources" / "testing"
        val buildResult = build(projectDir, ":foo:run", """--args=123456 abcd""")
        val result = buildResult.task(":foo:run")!!

        assertEquals(
            TaskOutcome.FAILED,
            result.outcome,
        )
        assertTrue("""java.lang.IllegalArgumentException: a.length <= 4, was 123456""" in buildResult.output)
    }

    @Test
    fun bTooShort() {
        val projectDir = fixtureDir / "resources" / "testing"
        val buildResult = build(projectDir, ":foo:run", """--args=123 a""")
        val result = buildResult.task(":foo:run")!!

        assertEquals(
            TaskOutcome.FAILED,
            result.outcome,
        )
        assertTrue("""java.lang.IllegalArgumentException: b.length >= 2, was a""" in buildResult.output)
    }

    @Test
    fun bTooLong() {
        val projectDir = fixtureDir / "resources" / "testing"
        val buildResult = build(projectDir, ":foo:run", """--args=123 abcdef""")
        val result = buildResult.task(":foo:run")!!

        assertEquals(
            TaskOutcome.FAILED,
            result.outcome,
        )
        assertTrue("""java.lang.IllegalArgumentException: b.length <= 4, was abcdef""" in buildResult.output)
    }

    private fun build(projectDir: Path, vararg tasks: String): BuildResult {
        return GradleRunner.create()
            .withPluginClasspath()
            .forwardOutput()
            .withProjectDir(projectDir.toFile())
            .withArguments("clean", *tasks, "--configuration-cache")
            .run()
    }
}
