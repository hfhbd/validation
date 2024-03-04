plugins {
    id("mpp")
}

kotlin.sourceSets.commonMain {
    dependencies {
        api(libs.serialization.core)
    }
}
