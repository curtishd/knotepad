import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "2.0.20"
}

group = "me.cdh"
version = "1.0"
application {
    mainClass.set("me.cdh.Main")
    mainModule.set("me.cdh")
    executableDir = "run"
}
repositories {
    mavenCentral()
}
apply {
    plugin("java-library")
    plugin("kotlin")
}

dependencies {
    implementation("com.formdev:flatlaf:3.5.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
val compileKotlin: KotlinCompile by tasks
val compileJava: JavaCompile by tasks
compileKotlin.destinationDirectory.set(compileJava.destinationDirectory)
tasks {
    run.configure {
        dependsOn(jar)
        doFirst {
            jvmArgs = listOf(
                "--module-path", classpath.asPath
            )
            classpath = files()
        }
    }

    compileJava {
        dependsOn(compileKotlin)
        doFirst {
            options.compilerArgs = listOf(
                "--module-path", classpath.asPath
            )
        }
    }

    compileKotlin {
        destinationDirectory.set(compileJava.get().destinationDirectory)
    }

    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}
tasks.jar {
    archiveFileName = "notepad.jar"
    manifest {
        attributes(mapOf("Main-Class" to "me.cdh.Main"))
    }
    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
    val sourcesMain = sourceSets.main.get()
    from(sourcesMain.output)
}