plugins {
    id("jvm")
    id("io.github.hfhbd.kotlin-compiler-testing")
}

kotlin.compilerOptions {
    optIn.add("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
    freeCompilerArgs.add("-Xcontext-parameters")
}

dependencies {
    annotationsRuntime(projects.runtime)
    annotationsRuntime(libs.serialization.core)
}

val generateTests by tasks.existing(JavaExec::class) {
    mainClass.set("app.softwork.validation.plugin.kotlin.GenerateTestsKt")
}
