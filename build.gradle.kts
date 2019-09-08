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
                // Setup the Kotlin compiler options for the 'main' compilation:
                jvmTarget = "1.8"
            }

//            compileKotlinTask // get the Kotlin task 'compileKotlinJvm'
//            output // get the main compilation output
        }

//        compilations["test"].runtimeDependencyFiles // get the test runtime classpath
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

//dependencies {
//    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.50")
//    compile ("org.jetbrains.kotlin:kotlin-reflect:1.3.50")
//
//}

//tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).all {
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//}