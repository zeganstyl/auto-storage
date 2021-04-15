plugins {
    kotlin("jvm") version "1.4.31"
    kotlin("plugin.serialization") version "1.4.31"
    application
}

group = "com.autostorage"
version = "0.1.0"

repositories {
    jcenter()
    mavenLocal()
}

val ktor_version = "1.5.2"
val exposed_version = "0.28.1"

dependencies {
    implementation("org.postgresql:postgresql:42.2.19")

    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jodatime:$exposed_version")

    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-freemarker:$ktor_version")

    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")

    implementation("com.zaxxer:HikariCP:4.0.2")

    implementation("org.slf4j:slf4j-simple:1.7.30")
}

val main = "com.autostorage.ServerKt"

application {
    mainClassName = main
}

tasks.getByName<JavaExec>("run") {
    dependsOn(tasks.getByName<Jar>("jar"))
    classpath(tasks.getByName<Jar>("jar"))
}

tasks.jar {
    archiveVersion.set("")

    manifest {
        attributes(
            mapOf(
                "Main-Class" to main
            )
        )
    }

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

tasks.create("stage").dependsOn("build", "clean")
tasks.getByName("build").mustRunAfter("clean")
