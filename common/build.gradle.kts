plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
}

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/korlibs/korlibs/") }
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
        all {
            languageSettings.useExperimentalAnnotation("kotlin.Experimental")
        }
        @Suppress("UNUSED_VARIABLE") val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.13.0")
                implementation("com.github.aakira:napier:0.0.8")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.1")
//                implementation("com.soywiz:korim:1.6.6")
                // not working right now!
//                implementation("com.russhwolf:multiplatform-settings:0.3.3")
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
                implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.50")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.13.0")
                implementation("com.github.aakira:napier-jvm:0.0.8")

                //                implementation("org.apache.xmlgraphics:batik-svg-dom:1.11")
                //                implementation("org.apache.xmlgraphics:batik-swing:1.11")
                //                implementation("org.apache.xmlgraphics:batik-svggen:1.11")
//                implementation("com.russhwolf:multiplatform-settings-jvm:0.3.3")
            }
        }
        javaFx.compilations["test"].defaultSourceSet {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
