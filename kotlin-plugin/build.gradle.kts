plugins {
	id("jvm")
}

kotlin.compilerOptions {
	optIn.add("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
}

dependencies {
	implementation(projects.model)
	compileOnly(libs.kotlin.compiler)

	testImplementation(libs.kotlinCompilerTester)
	testImplementation(kotlin("test"))

	// For use in tests.
	// TODO Use dedicated configuration and inject classpath as test system property.
	testImplementation(projects.runtime)
}
