dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

includeBuild("../../../../../")

include(":foo")
