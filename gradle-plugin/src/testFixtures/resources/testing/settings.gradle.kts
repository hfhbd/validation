dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "testing"

includeBuild("../../../../../")

include(":foo")
