plugins {
    id("jvm")
}

kotlin.compilerOptions {
    optIn.add("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
}

dependencies {
    compileOnly(libs.kotlin.compiler)

    testImplementation(kotlin("test"))
    testImplementation(libs.kotlinCompilerTester)
    testImplementation(libs.kotlin.compiler.embeddable)
    testImplementation(projects.runtime)
}
