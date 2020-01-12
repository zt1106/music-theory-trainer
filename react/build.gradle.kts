plugins {
    id("org.jetbrains.kotlin.js")
}

version = "unspecified"

repositories {
    mavenCentral()
}

kotlin.target.browser { }

kotlin {
    sourceSets["main"].dependencies {
        implementation(kotlin("stdlib-js"))
        implementation(npm("react", "16.8.3"))
    }
}