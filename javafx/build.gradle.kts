plugins {
    kotlin("jvm")
    id("javafx-gradle-plugin")
//    id("no.tornado.fxlauncher")
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}
tasks.compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    // kotlin jdk
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.50")
    implementation("no.tornado:tornadofx:1.7.17")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.3.1")
    implementation(project(":common"))
}

//fxlauncher {
//    applicationUrl = "file:///C:/Users/zt110/Desktop/mtt-fx"
//    deployTarget = "C:/Users/zt110/Desktop/mtt-fx"
//    applicationMainClass = "cc.zengtian.mtt.FxApp"
//}

jfx {
    // minimal requirement for jfxJar-task
    mainClass = "cc.zengtian.mtt.FxApp"

    // minimal requirement for jfxNative-task
    vendor = "YourName"
}