plugins {
    kotlin("multiplatform") version "1.3.50"
}

group = "cc.zengtian"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


kotlin {
    val javaFx = jvm("javaFx") {
        @Suppress("UNUSED_VARIABLE") val main by compilations.getting {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    sourceSets {
        @Suppress("UNUSED_VARIABLE") val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        @Suppress("UNUSED_VARIABLE") val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        javaFx.compilations["main"].defaultSourceSet {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("no.tornado:tornadofx:1.7.17")
                implementation("org.apache.xmlgraphics:batik-svg-dom:1.11")
                implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.50")
            }
        }
        javaFx.compilations["test"].defaultSourceSet {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}