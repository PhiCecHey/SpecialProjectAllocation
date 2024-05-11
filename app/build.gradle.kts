/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/8.0.2/userguide/building_java_projects.html
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
//import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application

    id("org.jetbrains.kotlin.jvm") version "1.7.0-RC2"
    id("java")

    // https://github.com/TheBoegl/gradle-launch4j
    id("edu.sc.seis.launch4j") version "2.5.4"

    // https://github.com/johnrengelman/shadow
    //id("com.github.johnrengelman.shadow") version "7.0.0"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()

    maven {
        url = uri("https://github.com/TheBoegl/gradle-launch4j")
    }

    gradlePluginPortal()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.1-jre")

    // https://mvnrepository.com/artifact/com.gurobi/gurobi
    //implementation("com.gurobi:gurobi:10.0.3")

    // import gurobi library:
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))

    // https://mvnrepository.com/artifact/com.miglayout/miglayout-swing
    //implementation("com.miglayout:miglayout-swing:11.2")

    // https://mvnrepository.com/artifact/com.miglayout/miglayout-core
    //implementation("com.miglayout:miglayout-core:11.2")

    // https://mvnrepository.com/artifact/com.miglayout/miglayout-javafx
    //implementation("com.miglayout:miglayout-javafx:11.2")

    //https://github.com/tools4j/decimal4j
    implementation("org.decimal4j:decimal4j:1.0.3")

    // https://github.com/JFormDesigner/FlatLaf
    implementation("com.formdev:flatlaf:3.4")
    implementation("com.formdev:flatlaf-intellij-themes:3.4")

    //implementation("com.github.johnrengelman:shadow:7.0.0")
    
    // https://github.com/ebourg/jsign
    //implementation("net.jsign:jsign-gradle-plugin:4.2")
}

application {
    // Define the main class for the application.
    mainClass.set("specialprojectallocation.App")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

// https://github.com/TheBoegl/gradle-launch4j
launch4j { // TODO: configure
  mainClassName = "specialprojectallocation.App"
  icon = "../../../app/icon/Logo_pforta.ico"
  outfile = "../../../release/${rootProject.name}.exe"
  // jarTask =  tasks.fatJar
  bundledJrePath = "jre"
  chdir = "."
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.register("prepareKotlinBuildScriptModel") {}

/*task("sign") { // https://ebourg.github.io/jsign/
    doLast {
        val jsign = project.extensions.getByName("jsign") as groovy.lang.Closure<*>
        jsign("file"      to "release/specialprojectallocation.exe",
            "name"      to "specialprojectallocation",
            "url"       to "specialprojectallocation.App",
            "keystore"  to "keystore.p12",
            "alias"     to "test",
            "storepass" to "secret",
            "tsaurl"    to "http://timestamp.sectigo.com")
    }
}*/

tasks {
    val fatJar = register<Jar>("fatJar") {
        dependsOn.addAll(listOf("compileJava", "compileKotlin", "processResources")) // We need this for Gradle optimization to work
        archiveClassifier.set("fatJar") // Naming the jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest { attributes(mapOf("Main-Class" to application.mainClass)) } // Provided we set it up in the application plugin configuration
        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } +
                sourcesMain.output
        from(contents)
    }
    build {
        dependsOn(fatJar) // Trigger fat jar creation during build
    }
}
