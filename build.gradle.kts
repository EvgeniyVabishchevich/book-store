plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {

    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")

    implementation("org.apache.tomcat.embed:tomcat-embed-core:10.1.11")
    implementation("org.apache.tomcat.embed:tomcat-embed-jasper:10.1.11")
    implementation("org.apache.tomcat:tomcat-jsp-api:10.1.11")

    implementation("com.google.inject:guice:7.0.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
    implementation("commons-io:commons-io:2.13.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("org.mockito:mockito-core:5.4.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.4.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.andersen.EntryPoint"
    }
}

application {
    mainClass.set("com.andersen.EntryPoint")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    reports.html.required.set(true)
    testLogging {
        showStandardStreams = true
    }
}
