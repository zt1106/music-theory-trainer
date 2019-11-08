plugins {
    id("org.jetbrains.kotlin.js")
}

kotlin {
    target {
        useCommonJs()
        nodejs()
    }

    sourceSets["main"].dependencies {
        implementation(kotlin("stdlib-js"))

        implementation(npm("chalk", "2.4.2"))
    }
}

repositories {
    mavenLocal()
    jcenter()
    maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
    maven(url = "https://dl.bintray.com/kotlin/kotlin-dev")
    maven(url = "https://dl.bintray.com/kotlin/kotlinx")
}