plugins {
    id("mpp")
}

val java9 by java.sourceSets.registering

tasks.jvmJar {
    into("META-INF/versions/9") {
        from(java9.map { it.output })
    }

    manifest.attributes("Multi-Release" to true)
}

tasks.named<JavaCompile>("compileJava9Java") {
    javaCompiler.set(javaToolchains.compilerFor {})
    options.release.set(9)

    options.compilerArgumentProviders += object : CommandLineArgumentProvider {

        @InputFiles
        @PathSensitive(PathSensitivity.RELATIVE)
        val kotlinClasses = tasks.compileKotlinJvm.flatMap { it.destinationDirectory }

        override fun asArguments(): List<String> = listOf(
            "--patch-module",
            "app.softwork.validation.runtime=${kotlinClasses.get().asFile.absolutePath}"
        )
    }
}
