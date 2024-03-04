plugins {
    id("jvm")
}

kotlin.compilerOptions {
    optIn.add("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
}

dependencies {
    implementation(projects.model)
    compileOnly(libs.kotlin.compiler)

    testImplementation(kotlin("test"))
    testImplementation(libs.kotlinCompilerTester)
    testImplementation(projects.runtime)
}
