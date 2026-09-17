plugins {
    id("jvm")
    id("io.github.hfhbd.kotlin-compiler-testing")
}

kotlin.compilerOptions {
    optIn.add("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
}

kotlinTesting {
    mainClass = "io.github.hfhbd.validation.plugin.kotlin.GenerateTestsKt"

    dependencies {
        annotation(projects.runtime)
        annotation(libs.serialization.core)
    }
}
