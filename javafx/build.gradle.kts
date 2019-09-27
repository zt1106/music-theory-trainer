plugins {
    kotlin("jvm")
    id("java")
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
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.50")
    compile("no.tornado:tornadofx:1.7.17")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.3.1")
    compile(project(":common"))
}

//fxlauncher {
//    applicationUrl = "file:///C:/Users/zt110/Desktop/mtt-fx"
//    deployTarget = "C:/Users/zt110/Desktop/mtt-fx"
//    applicationMainClass = "cc.zengtian.mtt.FxApp"
//}

jfx {
//    isVerbose = true
    mainClass = "cc.zengtian.mtt.JavaFxApp"
//    jfxAppOutputDir = "build/jfx/app"
//    jfxMainAppJarName = "my-application.jar"
//    deployDir = "src/main/deploy"
    isUseEnvironmentRelativeExecutables = true
//    libFolderName = "lib"


    // gradle jfxJar
//    isCss2bin = false
//    preLoader = null // String
//    isUpdateExistingJar = false
//    isAllPermissions = false
//    manifestAttributes = null // Map<String, String>
//    isAddPackagerJar = true
//    isCopyAdditionalAppResourcesToJar = false
    isSkipCopyingDependencies = false
//    isUseLibFolderContentForManifestClasspath = false
//    fixedManifestClasspath = null


    // gradle jfxNative
//    identifier = null                      // String - setting this for windows-bundlers makes it possible to generate upgradeable installers (using same GUID)
    vendor = "Company Name"
//    nativeOutputDir = "build/jfx/native"
//    bundler = "dmg"
//    jvmProperties = null                   // Map<String, String>
//    jvmArgs = null                         // List<String>
//    userJvmArgs = null                     // Map<String, String>
//    launcherArguments = null               // List<String>
//    nativeReleaseVersion = "$version"
//    isNeedShortcut = false
//    isNeedMenu = false

    //bundleArguments = [
    //compile: null, // dont bundle JRE, useful for faster builds during testing
    //licenseType: 'ASL 2.0',
    //licenseFile: 'LICENSE'
    //]

//    appName = "My Application"                // this is used for files below "src/main/deploy", e.g. "src/main/deploy/package/windows/project.ico"
//    additionalBundlerResources = null         // path to some additional resources for the bundlers when creating application-bundle
//    additionalAppResources = null             // path to some additional resources when creating application-bundle
//    fileAssociations = null                   // List<Map<String, Object>>
//    isNoBlobSigning = false                     // when using bundler "jnlp", you can choose to NOT use blob signing
//    customBundlers = null                     // List<String>
//    isFailOnError = true
//    isOnlyCustomBundlers = false
//    isSkipJNLP = true
//    isSkipNativeVersionNumberSanitizing = false // anything than numbers or dots are removed
//    additionalJarsignerParameters = null      // List<String>
//    isSkipMainClassScanning = false             // set to true might increase build-speed

//    isSkipNativeLauncherWorkaround124 = false
//    isSkipNativeLauncherWorkaround167 = false
//    isSkipNativeLauncherWorkaround205 = false
//    isSkipJNLPRessourcePathWorkaround182 = false
//    isSkipSigningJarFilesJNLP185 = false
//    isSkipSizeRecalculationForJNLP185 = false
//    isSkipMacBundlerWorkaround = false
}
