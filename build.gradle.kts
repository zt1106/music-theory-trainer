plugins {
    id ("org.jetbrains.kotlin.jvm") version "1.3.50"
}

group = "cc.zengtian"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
}

dependencies {
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.50")
    compile ("org.jetbrains.kotlin:kotlin-reflect:1.3.50")
    compile ("no.tornado:tornadofx:1.7.17")
    compile ("org.apache.xmlgraphics:batik-svg-dom:1.11")
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}