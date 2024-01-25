plugins {
    kotlin("jvm") version "1.9.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.8.+")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}